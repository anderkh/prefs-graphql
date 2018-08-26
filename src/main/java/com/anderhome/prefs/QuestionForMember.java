package com.anderhome.prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.anderhome.prefs.model.MemberProfileQuestion;
import com.anderhome.prefs.model.ProfileQuestion;
import io.leangen.graphql.annotations.GraphQLQuery;

/*
 * We need one of these for every question, regardless of whether or not the member has answered it
 */

public class QuestionForMember {
    // private static final long serialVersionUID = 1L; 
//	private static final Log LOGGER = LogFactory.getLog(QuestionForMember.class);

    private ProfileQuestion profileQuestion;
    private Optional<MemberProfileQuestion> memberProfileQuestion;
    
    public ProfileQuestion profileQuestion() {
    	return profileQuestion;
    }
    public void setProfileQuestion(ProfileQuestion aProfileQuestion) {
    	profileQuestion = aProfileQuestion;
    }
    
    public Optional<MemberProfileQuestion> memberProfileQuestion() {
    	return memberProfileQuestion;
    }
    public void setMemberProfileQuestion(MemberProfileQuestion aMemberProfileQuestion) {
    	memberProfileQuestion = Optional.ofNullable(aMemberProfileQuestion);
    }
    
    @GraphQLQuery
    public Integer profileQuestionId() {
    	return profileQuestion.getProfileQuestionId();
    }
    
    @GraphQLQuery
    public String descr() {
    	return profileQuestion.getDescr();
    }
    
    @GraphQLQuery
    public String title() {
    	return profileQuestion.getTitle();
    }
    
    @GraphQLQuery
    public boolean isActive() {
    	return profileQuestion.isIsActive();
    }
    
    @GraphQLQuery
    public Integer getOrdinal() {
    	return profileQuestion.getOrdinal();
    }
    
    @GraphQLQuery
    public List<AnswerForMember> profileQuestionOptions() {
    	    	
    	ArrayList<AnswerForMember> afms = new ArrayList<AnswerForMember>();
    	    	
    	profileQuestion.getProfileQuestionOptions().forEach(pqo -> {
    		AnswerForMember afm = new AnswerForMember();
    		afm.setProfileQuestionOption(pqo);
    		if (memberProfileQuestion.isPresent()) {
        		afm.setMemberProfileQuestionOption(memberProfileQuestion.get().memberProfileQuestionOptionForProfileQuestionOption(pqo));
    		}
    		afms.add(afm);
    	});
    	
    	return afms;
    }

}
