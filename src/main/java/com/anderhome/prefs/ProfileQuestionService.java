package com.anderhome.prefs;

import java.util.ArrayList;
import java.util.List;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anderhome.cayenne.ICayenneService;
import com.anderhome.prefs.ProfileQuestionService;
import com.anderhome.prefs.model.ProfileQuestion;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotation.GraphQLApi;

@GraphQLApi
@Service

public class ProfileQuestionService {
	@Autowired
	ICayenneService cayenneService;

    @GraphQLQuery(name="profileQuestion")
    public List<ProfileQuestion> profileQuestionById(@GraphQLArgument(name="id") Integer id) {
    	ObjectContext oc = cayenneService.newObjectContext();
    	ProfileQuestion profileQuestion = Cayenne.objectForPK(oc, ProfileQuestion.class, id);
    	
    	ArrayList<ProfileQuestion> result = new ArrayList<ProfileQuestion>();
    	result.add(profileQuestion);
        return result;
    }
    
    @GraphQLQuery(name="profileQuestion")
    public List<ProfileQuestion> profileQuestions() {
    	ObjectContext oc = cayenneService.newObjectContext();
    	List<ProfileQuestion> result = ObjectSelect.query(ProfileQuestion.class).select(oc);
    	
        return result;
    }
    
    @GraphQLQuery(name="profileQuestion")
    public List<ProfileQuestion> profileQuestions(@GraphQLArgument(name="memberId") Integer memberId) {
    	ObjectContext oc = cayenneService.newObjectContext();

    	List<ProfileQuestion> result = ObjectSelect.query(ProfileQuestion.class).select(oc);
    	
        return result;
    }
}
