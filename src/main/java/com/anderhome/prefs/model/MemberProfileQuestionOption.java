package com.anderhome.prefs.model;

import com.anderhome.prefs.model.auto._MemberProfileQuestionOption;
import io.leangen.graphql.annotations.GraphQLQuery;

public class MemberProfileQuestionOption extends _MemberProfileQuestionOption {

    private static final long serialVersionUID = 1L; 

    @GraphQLQuery
    public String questionOption() {
    	return this.getProfileQuestionOption().getQuestionOption();
    }

}
