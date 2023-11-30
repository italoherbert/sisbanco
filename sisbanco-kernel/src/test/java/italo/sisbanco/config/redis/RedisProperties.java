package italo.sisbanco.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisProperties {

	@Value("${spring.data.redis.host}")
	private String redisHost;
	
	@Value("${spring.data.redis.port}")
	private int redisPort;
	
	public String getRedisHost() {
		return redisHost;
	}
	
	public int getRedisPort() {
		return redisPort;
	}
	
}
