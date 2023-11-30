package italo.sisbanco.kernel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

	@Value("${spring.data.redis.password}")
	private String password;		
	
	/*
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();		
		conf.setPassword( RedisPassword.of( password ) );
		return new JedisConnectionFactory( conf );
	}
	
	@Bean
	RedisTemplate<String, TransacaoCache> redisTemplate() {
		RedisTemplate<String, TransacaoCache> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory( jedisConnectionFactory() );
		return redisTemplate;
	}
	*/
	
}
