package italo.sisbanco.historico.ext.mongodb;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@Import( MongoTestConfiguration.class )
public class MongoDBTest {	
	
	private static MongoDBContainer mongoContainer;
		
	static {
		mongoContainer = new MongoDBContainer( DockerImageName.parse( "mongo:7" ) );
		mongoContainer.start();						
	}	
		
	@DynamicPropertySource
	static void mongoDbProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);		
	}
			
}
