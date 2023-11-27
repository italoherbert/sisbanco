package italo.sisbanco.historico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import italo.sisbanco.shared.util.HttpUtil;

@Configuration
public class MainConfiguration {
	
	@Bean
	HttpUtil createHttpUtil() {
		return new HttpUtil();
	}

}
