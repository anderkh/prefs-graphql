package com.anderhome.prefs.model.auto;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.exp.Property;

import com.anderhome.prefs.model.MemberProfileQuestion;
import com.anderhome.prefs.model.ProfileQuestionOption;

/**
 * Class _MemberProfileQuestionOption was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _MemberProfileQuestionOption extends CayenneDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<Short> RANKING = Property.create("ranking", Short.class);
    public static final Property<MemberProfileQuestion> MEMBER_PROFILE_QUESTION = Property.create("memberProfileQuestion", MemberProfileQuestion.class);
    public static final Property<ProfileQuestionOption> PROFILE_QUESTION_OPTION = Property.create("profileQuestionOption", ProfileQuestionOption.class);

    public void setRanking(Short ranking) {
        writeProperty("ranking", ranking);
    }
    public Short getRanking() {
        return (Short)readProperty("ranking");
    }

    public void setMemberProfileQuestion(MemberProfileQuestion memberProfileQuestion) {
        setToOneTarget("memberProfileQuestion", memberProfileQuestion, true);
    }

    public MemberProfileQuestion getMemberProfileQuestion() {
        return (MemberProfileQuestion)readProperty("memberProfileQuestion");
    }


    public void setProfileQuestionOption(ProfileQuestionOption profileQuestionOption) {
        setToOneTarget("profileQuestionOption", profileQuestionOption, true);
    }

    public ProfileQuestionOption getProfileQuestionOption() {
        return (ProfileQuestionOption)readProperty("profileQuestionOption");
    }


}