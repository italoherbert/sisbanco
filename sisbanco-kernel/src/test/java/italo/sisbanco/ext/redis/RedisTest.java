package italo.sisbanco.ext.redis;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

@ActiveProfiles("test") 
@Testcontainers
@Import(RedisTestConfiguration.class)
public class RedisTest {
				
	@Container
	private static RedisContainer redis;
	
	static {
		redis = new RedisContainer(DockerImageName.parse("redis") );		
		redis.start();		
	}
	
	@DynamicPropertySource
	public static void registerRedisProperties(DynamicPropertyRegistry registry) {
	    registry.add("spring.data.redis.host", redis::getHost);
	    registry.add("spring.data.redis.port", () -> redis.getFirstMappedPort() );
	}		
	
}
