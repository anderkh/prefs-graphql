package com.anderhome.prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.anderhome.cayenne.ICayenneService;
import com.anderhome.prefs.model.Member;
import com.anderhome.prefs.model.MemberProfileQuestion;
import com.anderhome.prefs.model.MemberProfileQuestionOption;
import com.anderhome.prefs.model.ProfileQuestion;
import com.anderhome.prefs.model.ProfileQuestionOption;
import com.anderhome.prefs.MemberService;
import com.anderhome.prefs.QuestionForMember;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotation.GraphQLApi;

@GraphQLApi
@Service
public class MemberService {
	
	@Autowired
	ICayenneService cayenneService;
	
	private static final Log LOGGER = LogFactory.getLog(MemberService.class);

    @GraphQLQuery(name="member")
    public Member member(@GraphQLArgument(name="id") Integer id,
    					 @GraphQLArgument(name="sessionToken") String sessionToken) throws InvalidSessionTokenException {
    	ObjectContext oc = cayenneService.newObjectContext();
    	Member member = Cayenne.objectForPK(oc, Member.class, id);
    	
    	if (sessionToken != null && sessionToken.hashCode() == member.getSessionTokenHash().intValue()) {
    		return member;
    	} else {
    		throw new InvalidSessionTokenException("[401] Invalid Session Token");
    	}
    }
    
    @GraphQLQuery
    public List<QuestionForMember> profileQuestions(@GraphQLContext Member member) {
    	    	
    	ObjectContext oc = member.getObjectContext();
    	ArrayList<QuestionForMember> qfms = new ArrayList<QuestionForMember>();
    	
    	List<ProfileQuestion> questions = ObjectSelect.query(ProfileQuestion.class).where(ProfileQuestion.IS_ACTIVE.eq(true)).select(oc);
    	questions.forEach(pq -> {
        	QuestionForMember qfm = new QuestionForMember();
        	qfm.setProfileQuestion(pq);
        	qfm.setMemberProfileQuestion(member.memberProfileQuestionForProfileQuestion(pq));
        	qfms.add(qfm);
    	});
    	return qfms;
    }

    @GraphQLMutation
    public Member createEmailMember(@GraphQLArgument(name="firstName") String firstName,
    							@GraphQLArgument(name="lastName") String lastName,
    							@GraphQLArgument(name="emailAddress") String emailAddress,
    							@GraphQLArgument(name="phoneNumber") String phoneNumber,
    							@GraphQLArgument(name="photo") byte[] photo,
    							@GraphQLArgument(name="password") String password) throws DuplicateEmailException {

    	ObjectContext oc = cayenneService.newObjectContext();
    	
    	Member existingMember = ObjectSelect.query(Member.class).where(Member.EMAIL_ADDRESS.eq(emailAddress)).selectOne(oc);
    	if (existingMember == null) {
	    	Member newMember = oc.newObject(Member.class);
	    	newMember.setFirstName(firstName);
	    	newMember.setLastName(lastName);
	    	newMember.setPhoneNumber(phoneNumber);
	    	newMember.setEmailAddress(emailAddress);
	    	newMember.setPhoto(photo);
	    	
	    	String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt()); 
	    	newMember.setBcryptPassword(pw_hash);
	    	
	        UUID uuid = UUID.randomUUID();
	        String randomUUIDString = uuid.toString();
	        
	        newMember.setSessionToken(randomUUIDString);
	        newMember.setSessionTokenHash(randomUUIDString.hashCode());
	
	    	oc.commitChanges();
	    	return newMember;
    	} else {
    		throw new DuplicateEmailException("[500] Email address already exists");
    	}
    }
    
    @GraphQLMutation
    public Member emailLogin(@GraphQLArgument(name="emailAddress") String emailAddress,
    						@GraphQLArgument(name="password") String password) throws InvalidAuthenticationException {
    	
    	ObjectContext oc = cayenneService.newObjectContext();

    	List<Member> users = ObjectSelect.query(Member.class).where(Member.EMAIL_ADDRESS.eq(emailAddress)).select(oc);
    	
    	if (users.size() == 1) {
    		Member member = users.get(0);
    		if (BCrypt.checkpw(password, member.getBcryptPassword())) {
    			UUID uuid = UUID.randomUUID();
    			String randomUUIDString = uuid.toString();
            
    			member.setSessionToken(randomUUIDString);
    			member.setSessionTokenHash(randomUUIDString.hashCode());
    			oc.commitChanges();
    			return member;
    		} else {
    			throw new InvalidAuthenticationException("[401] Invalid Authentication");
    		}
    	} else if (users.size() > 1) {
    		LOGGER.error("email address "+emailAddress+" returned more than one row!");
    		return null;
    	} else {
    		return null;
    	}
    }
    
    @GraphQLMutation
    public Member setRankingForOrderedProfileQuestion(@GraphQLArgument(name="memberId") Integer memberId,
    											@GraphQLArgument(name="sessionToken") String sessionToken,
    											@GraphQLArgument(name="profileQuestionId") Integer profileQuestionId,
    											@GraphQLArgument(name="profileQuestionOptionIds") List<Integer> profileQuestionOptionIds) throws InvalidSessionTokenException {
    	ObjectContext oc = cayenneService.newObjectContext();
    	
    	Member member = Cayenne.objectForPK(oc, Member.class, memberId);
    	if (sessionToken.hashCode() == member.getSessionTokenHash().intValue()) {
    	
	    	ProfileQuestion profileQuestion = Cayenne.objectForPK(oc, ProfileQuestion.class, profileQuestionId);
	    	
	    	MemberProfileQuestion memberProfileQuestion = ObjectSelect.query(MemberProfileQuestion.class).where(MemberProfileQuestion.MEMBER.eq(member).andExp(MemberProfileQuestion.PROFILE_QUESTION.eq(profileQuestion))).selectOne(oc);
	
	    	if (memberProfileQuestion == null) {
	    		memberProfileQuestion = oc.newObject(MemberProfileQuestion.class);
	    		memberProfileQuestion.setMember(member);
	    		memberProfileQuestion.setProfileQuestion(profileQuestion);
	    	}
	    	
	    	for (MemberProfileQuestionOption mpqo : new ArrayList<MemberProfileQuestionOption>(memberProfileQuestion.getMemberProfileQuestionOptions())) {
	    		memberProfileQuestion.removeFromMemberProfileQuestionOptions(mpqo);
	    		oc.deleteObject(mpqo);
	    	}
	    	
	    	int rank = 1;
	    	for (Integer optionId : profileQuestionOptionIds) {
	    		ProfileQuestionOption pqo = Cayenne.objectForPK(oc,  ProfileQuestionOption.class, optionId);
	    		this.addProfileQuestionRank(memberProfileQuestion, pqo, rank);
	    		rank++;
	    	}
	
	    	oc.commitChanges();
    	} else {
    		throw new InvalidSessionTokenException("[401] Invalid Session Token");
    	}

    	return member;
    }
    
    private void addProfileQuestionRank(MemberProfileQuestion question, ProfileQuestionOption option, int rank) {
    	MemberProfileQuestionOption mpqo = question.getObjectContext().newObject(MemberProfileQuestionOption.class);
    	mpqo.setMemberProfileQuestion(question);
    	mpqo.setProfileQuestionOption(option);
    	mpqo.setRanking((short) rank);
    	question.addToMemberProfileQuestionOptions(mpqo);
    }
    
    public class DuplicateEmailException extends Exception {
    	static final long serialVersionUID = 1;
    	
    	public DuplicateEmailException(String error) {
    		super(error);
    	}
    }
    
    public class InvalidSessionTokenException extends Exception {
    	static final long serialVersionUID = 1;
    	
    	public InvalidSessionTokenException(String error) {
    		super(error);
    	}
    }
    
    public class InvalidAuthenticationException extends Exception {
    	static final long serialVersionUID = 1;
    	
    	public InvalidAuthenticationException(String error) {
    		super(error);
    	}
    }

}

