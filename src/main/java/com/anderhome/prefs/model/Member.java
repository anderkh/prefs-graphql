package com.anderhome.prefs.model;

import java.util.Optional;

import com.anderhome.prefs.model.auto._Member;
import com.anderhome.prefs.model.MemberProfileQuestion;
import com.anderhome.prefs.model.ProfileQuestion;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;

public class Member extends _Member {

    private static final long serialVersionUID = 1L; 
    
    private String sessionToken;

    public Integer getMemberId() {
        return (getObjectId() != null && !getObjectId().isTemporary()) 
                 ? (Integer)getObjectId().getIdSnapshot().get(ID_PK_COLUMN) 
                 : null;
     }

    public void setSessionToken(String token) {
    	sessionToken = token;
    }
    @GraphQLQuery
    public String sessionToken() {
    	return sessionToken;
    }
    
    @GraphQLIgnore
    public String getBcryptPassword() {
    	return super.getBcryptPassword();
    }
    
    @GraphQLIgnore
    public Integer getSessionTokenHash() {
    	return super.getSessionTokenHash();
    }
    
    public MemberProfileQuestion memberProfileQuestionForProfileQuestion(ProfileQuestion profileQuestion) {
    	Optional<MemberProfileQuestion> result = this.getMemberProfileQuestions().stream().filter(mpq -> mpq.getProfileQuestion().equals(profileQuestion)).findFirst();
    	if (result.isPresent())
    		return result.get();
    	else
    		return null;
    }

}
