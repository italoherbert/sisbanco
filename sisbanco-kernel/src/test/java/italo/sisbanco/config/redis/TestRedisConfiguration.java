package italo.sisbanco.config.redis;

import org.springframework.boot.test.context.TestConfiguration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

@TestConfiguration
public class TestRedisConfiguration {
			
	private RedisServer redisServer;
		
	public TestRedisConfiguration( RedisProperties props ) {
		redisServer = RedisServer.builder()
				.port( props.getRedisPort() )
				.setting("maxmemory 128M")
				.build();
	}
	
	@PostConstruct
	public void postConstruct() {
		redisServer.start();		
	}
	
	@PreDestroy
	public void preDestroy() {
		redisServer.stop();				
	}	
	
}
