package com.anderhome.cayenne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.commons.logging.Log;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Service(value="com.anderhome.cayenne.ICayenneService")

public class CayenneService implements ICayenneService {

	private static final Log LOGGER = LogFactory.getLog(CayenneService.class);

	private ObjectContext sharedContext;
	private ServerRuntime cayenneRuntime;

	@Value("#{systemProperties['cayenne.project.context']}")
	private String config;

	@Autowired
	private Environment environment;

	@PostConstruct
	void init() {		
		HikariConfig confix = new HikariConfig();
		confix.setJdbcUrl(environment.getProperty("cayenne.jdbc.url"));
		confix.setUsername(environment.getProperty("cayenne.jdbc.username"));
		confix.setPassword(environment.getProperty("cayenne.jdbc.password"));
		confix.setMaximumPoolSize(Integer.valueOf( environment.getProperty("cayenne.jdbc.max_connections")));
		confix.setDriverClassName(environment.getProperty("cayenne.jdbc.driver"));
		confix.setConnectionTestQuery("SELECT 1");
		
		confix.addDataSourceProperty("socketTimeout", 1);
		confix.setMinimumIdle(0);
		confix.setIdleTimeout(1000);
		
		confix.setConnectionTimeout(20000);
		confix.setLeakDetectionThreshold(300000); //5 mins.
		HikariDataSource ds = new HikariDataSource(confix);

        cayenneRuntime = ServerRuntime.builder()
                .addConfig("cayenne-project.xml")
                .dataSource(ds)
                .build();
        
		sharedContext = cayenneRuntime.newContext();

		Runtime.getRuntime().addShutdownHook(new Thread( new Runnable() {
			
			@Override
			public void run() {
				if (cayenneRuntime != null) {
					cayenneRuntime.shutdown();
				}
			}
		}));
		
		LOGGER.info("Cayenne setup done");
		LOGGER.info(this);
	}

	@Override
	public ObjectContext sharedContext() {
		return sharedContext;
	}

	@Override
	public ObjectContext newObjectContext() {
		return cayenneRuntime.newContext();
	}
	
	@PreDestroy
	public void shutdown(){
		if (cayenneRuntime != null)
			cayenneRuntime.shutdown();
	}
}
