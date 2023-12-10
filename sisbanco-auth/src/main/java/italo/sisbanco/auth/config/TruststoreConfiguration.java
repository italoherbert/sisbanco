package italo.sisbanco.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;

@Configuration
public class TruststoreConfiguration {

	@Autowired
	private Environment env;
	
	@PostConstruct
	public void init() {
		System.setProperty( "javax.net.ssl.trustStore", env.getProperty( "config.truststore.file" ) ); 
		System.setProperty( "javax.net.ssl.trustStorePassword", env.getProperty( "config.truststore.password" ) ); 
	}
	
}
