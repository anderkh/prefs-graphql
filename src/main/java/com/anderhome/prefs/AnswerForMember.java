package com.anderhome.prefs;

import java.util.Optional;

import com.anderhome.prefs.model.MemberProfileQuestionOption;
import com.anderhome.prefs.model.ProfileQuestionOption;

import io.leangen.graphql.annotations.GraphQLQuery;

public class AnswerForMember {
	private ProfileQuestionOption profileQuestionOption;
	private Optional<MemberProfileQuestionOption> memberProfileQuestionOption;
	
	public ProfileQuestionOption profileQuestionOption() {
		return profileQuestionOption;
	}
	public void setProfileQuestionOption(ProfileQuestionOption apqo) {
		profileQuestionOption = apqo;
	}
	
	public Optional<MemberProfileQuestionOption> memberProfileQuestionOption() {
		return memberProfileQuestionOption;
	}
	public void setMemberProfileQuestionOption(Optional<MemberProfileQuestionOption> ampqo) {
		memberProfileQuestionOption = ampqo;
	}
	
	@GraphQLQuery
	public Integer profileQuestionOptionId() {
		return profileQuestionOption.getProfileQuestionOptionId();
	}
	
	@GraphQLQuery
	public String questionOption() {
		return profileQuestionOption.getQuestionOption();
	}
	
	@GraphQLQuery
	public Integer ranking() {
		if (memberProfileQuestionOption != null && memberProfileQuestionOption.isPresent())
			return new Integer(memberProfileQuestionOption.get().getRanking());
		else
			return Integer.MAX_VALUE;
	}
}
