package com.anderhome.prefs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anderhome.cayenne.CayenneService;
import com.anderhome.cayenne.ICayenneService;
import com.anderhome.prefs.MemberService;
import com.anderhome.prefs.ProfileQuestionService;

@Configuration
@SpringBootApplication
public class PrefsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrefsApplication.class, args);
	}
	
    @Bean
    public MemberService memberService() {
        return new MemberService();
    }

    @Bean
    public ProfileQuestionService profileQuestionService() {
        return new ProfileQuestionService();
    }
    
    @Bean
    public ICayenneService cayenneService() {
    	return new CayenneService();
    }
}
