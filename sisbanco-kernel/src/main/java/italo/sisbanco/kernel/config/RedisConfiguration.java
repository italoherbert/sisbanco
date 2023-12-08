package italo.sisbanco.kernel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.cache.TransacaoCache;

@Configuration
@Profile("!test")
public class RedisConfiguration {

	@Value("${spring.data.redis.host}")
	private String host;
	
	@Value("${spring.data.redis.port}")
	private int port;
	
	@Value("${spring.data.redis.password}")
	private String password;		
			
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration( host, port );		
		conf.setPassword( RedisPassword.of( password ) );
		return new JedisConnectionFactory( conf );
	}
	
	@Bean
	RedisTemplate<String, TransacaoCache> transacaoRedisTemplate( 
			JedisConnectionFactory connectionFactory ) {
		RedisTemplate<String, TransacaoCache> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory( connectionFactory );
		return redisTemplate;
	}	
	
	@Bean
	RedisTemplate<String, AlteraValorEmContaCache> alterValorEmContaRedisTemplate( 
			JedisConnectionFactory connectionFactory ) {
		RedisTemplate<String, AlteraValorEmContaCache> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory( connectionFactory );
		return redisTemplate;
	}
	
}
