package italo.sisbanco.historico.ext.mongodb;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;

@Configuration
public class MongoConfiguration {
	
	@Bean
	MongoClient mongoClient() {
		return mock( MongoClient.class );
	}
	
}
