package italo.sisbanco.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class TruststoreConfiguration {
	
	@Value("${config.truststore.file:}")
	private String trustStoreFile;
	
	@Value("${config.truststore.password:}")
	private String trustStorePassword;
	
	@PostConstruct
	public void init() {		
		System.setProperty( "javax.net.ssl.trustStore", trustStoreFile ); 
		System.setProperty( "javax.net.ssl.trustStorePassword", trustStorePassword ); 
	}
	
}
