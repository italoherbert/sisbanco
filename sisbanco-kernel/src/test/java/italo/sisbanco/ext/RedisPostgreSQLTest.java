package italo.sisbanco.ext;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

import italo.sisbanco.ext.redis.RedisTestConfiguration;

@ActiveProfiles("test") 
@Testcontainers
@Import(RedisTestConfiguration.class)
public class RedisPostgreSQLTest {
	
	private static PostgreSQLContainer<?> postgreSQLContainer;		
	private static RedisContainer redis;
	
	static {
		postgreSQLContainer = new PostgreSQLContainer<>( DockerImageName.parse( "postgres" ) )
				.withUsername( "postgres" )
				.withPassword( "postgres" )
				.withDatabaseName( "test" );
		postgreSQLContainer.start();
		
		redis = new RedisContainer( DockerImageName.parse("redis") );		
		redis.start();		
	}
	
	@DynamicPropertySource
	public static void registerProperties(DynamicPropertyRegistry registry) {
	    registry.add( "spring.datasource.url", postgreSQLContainer::getJdbcUrl );
	    registry.add( "spring.datasource.username", postgreSQLContainer::getUsername );
	    registry.add( "spring.datasource.password", postgreSQLContainer::getPassword );
	
	    registry.add("spring.data.redis.host", redis::getHost);
	    registry.add("spring.data.redis.port", () -> redis.getFirstMappedPort() );
	}		
	
}
