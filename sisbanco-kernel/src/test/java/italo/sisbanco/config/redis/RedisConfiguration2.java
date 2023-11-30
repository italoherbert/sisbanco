package italo.sisbanco.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import italo.sisbanco.kernel.model.cache.TransacaoCache;

@Configuration
public class RedisConfiguration2 {
			
	@Bean
    LettuceConnectionFactory redisConnectionFactory(
      RedisProperties redisProperties) {
        return new LettuceConnectionFactory(
          redisProperties.getRedisHost(), 
          redisProperties.getRedisPort());
    }

    @Bean
    RedisTemplate<String, TransacaoCache> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, TransacaoCache> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
	
}
