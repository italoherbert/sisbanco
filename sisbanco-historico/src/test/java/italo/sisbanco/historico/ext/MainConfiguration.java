package italo.sisbanco.historico.ext;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import italo.sisbanco.historico.log.GlobalLogger;

@TestConfiguration
public class MainConfiguration {

	@Bean
	GlobalLogger globalLogger() {
		return mock( GlobalLogger.class );
	}
	
}
