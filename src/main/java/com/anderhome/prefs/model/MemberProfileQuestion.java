package com.anderhome.prefs.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.anderhome.prefs.model.auto._MemberProfileQuestion;
import com.anderhome.prefs.model.MemberProfileQuestionOption;
import com.anderhome.prefs.model.ProfileQuestionOption;

public class MemberProfileQuestion extends _MemberProfileQuestion {

    private static final long serialVersionUID = 1L; 

    public Optional<MemberProfileQuestionOption> memberProfileQuestionOptionForProfileQuestionOption(ProfileQuestionOption profileQuestionOption) {
    	
    	List<MemberProfileQuestionOption> aList = this.getMemberProfileQuestionOptions();
    	
    	Stream<MemberProfileQuestionOption> stream = aList.stream();
    	
    	Stream<MemberProfileQuestionOption> filteredStream = stream.filter(mpqo -> mpqo.getProfileQuestionOption().equals(profileQuestionOption));
    	
    	return filteredStream.findFirst();
    }

}
