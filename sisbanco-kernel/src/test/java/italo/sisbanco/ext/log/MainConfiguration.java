package italo.sisbanco.ext.log;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import italo.sisbanco.kernel.components.log.GlobalLogger;

@TestConfiguration
public class MainConfiguration {

	@Bean
	GlobalLogger logger() {
		return mock( GlobalLogger.class );
	}
	
}
