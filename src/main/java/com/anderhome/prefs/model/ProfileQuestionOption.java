package com.anderhome.prefs.model;

import com.anderhome.prefs.model.auto._ProfileQuestionOption;

public class ProfileQuestionOption extends _ProfileQuestionOption {

    private static final long serialVersionUID = 1L; 

    public Integer getProfileQuestionOptionId() {
        return (getObjectId() != null && !getObjectId().isTemporary()) 
                 ? (Integer)getObjectId().getIdSnapshot().get(ID_PK_COLUMN) 
                 : null;
     }
}
