package italo.sisbanco.ext.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import italo.sisbanco.kernel.model.cache.TransacaoCache;

@TestConfiguration
public class RedisTestConfiguration {
					
	@Bean
    LettuceConnectionFactory redisConnectionFactory(
    		@Value("${spring.data.redis.host}") String redisHost,
    		@Value("${spring.data.redis.port}") int redisPort) {
        return new LettuceConnectionFactory( redisHost, redisPort );
    }

    @Bean
    RedisTemplate<String, TransacaoCache> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, TransacaoCache> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
	
}

