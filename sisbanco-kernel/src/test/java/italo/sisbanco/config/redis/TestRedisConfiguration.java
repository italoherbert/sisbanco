package italo.sisbanco.config.redis;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import italo.sisbanco.kernel.model.cache.TransacaoCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

@TestConfiguration
public class TestRedisConfiguration {
				
	@Value("${spring.data.redis.host}")
	private String redisHost;
		
	@Value("${spring.data.redis.port}")
	private int redisPort;
	
	private RedisServer redisServer;
		
	public TestRedisConfiguration( @Value("${spring.data.redis.port}") int redisPort ) {
		try {
			redisServer = RedisServer.newRedisServer()
					.port( redisPort )
					.setting("maxmemory 128M")
					.build();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
			
	@Bean
    LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory( redisHost, redisPort );
    }

    @Bean
    RedisTemplate<String, TransacaoCache> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, TransacaoCache> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
	
	@PostConstruct
	public void postConstruct() {
		try {
			redisServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@PreDestroy
	public void preDestroy() {
		try {
			redisServer.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}	
	
}
