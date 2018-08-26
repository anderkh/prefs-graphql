package com.anderhome.prefs.model;

import com.anderhome.prefs.model.auto._ProfileQuestion;

public class ProfileQuestion extends _ProfileQuestion {

    private static final long serialVersionUID = 1L; 

    public Integer getProfileQuestionId() {
        return (getObjectId() != null && !getObjectId().isTemporary()) 
                 ? (Integer)getObjectId().getIdSnapshot().get(ID_PK_COLUMN) 
                 : null;
     }
}
