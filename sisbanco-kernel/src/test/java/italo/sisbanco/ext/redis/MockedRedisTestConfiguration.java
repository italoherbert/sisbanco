package italo.sisbanco.ext.redis;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.cache.TransacaoCache;

@TestConfiguration
public class MockedRedisTestConfiguration {

	@Bean
    LettuceConnectionFactory redisConnectionFactory() {
        return mock( LettuceConnectionFactory.class );
    }

    @Bean
    RedisTemplate<String, TransacaoCache> transacaoRedisTemplate( LettuceConnectionFactory connectionFactory ) {
        RedisTemplate<String, TransacaoCache> template = new RedisTemplate<>();
        template.setConnectionFactory( connectionFactory );
        return template;
    }
    
    @Bean
    RedisTemplate<String, AlteraValorEmContaCache> alterValorEmContaRedisTemplate( LettuceConnectionFactory connectionFactory ) {
     	 RedisTemplate<String, AlteraValorEmContaCache> template = new RedisTemplate<>();
         template.setConnectionFactory( connectionFactory );
         return template;
    }
	
}
