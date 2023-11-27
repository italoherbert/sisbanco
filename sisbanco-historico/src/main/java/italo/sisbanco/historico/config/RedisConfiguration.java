package italo.sisbanco.historico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import italo.sisbanco.historico.model.CacheTransacao;

@Configuration
public class RedisConfiguration {

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
		conf.setPassword( RedisPassword.of( "123" ) ); 
		return new JedisConnectionFactory( conf );
	}
	
	@Bean
	RedisTemplate<String, CacheTransacao> redisTemplate() {
		RedisTemplate<String, CacheTransacao> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory( jedisConnectionFactory() );
		return redisTemplate;
	}
	
}
