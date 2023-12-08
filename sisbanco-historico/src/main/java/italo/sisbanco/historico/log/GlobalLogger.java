package italo.sisbanco.historico.log;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import italo.sisbanco.shared.util.LoggerBuilder;

@Component
@Profile("!test") 
public class GlobalLogger {

	private final LoggerBuilder loggerBuilder = new LoggerBuilder();
	private Logger logger;
	
	public GlobalLogger( @Value("${config.global.error.log.file}") String errorLogFile ) {
		logger = loggerBuilder.build( "global-error-log", errorLogFile );
	}
	
	public void severe( String message ) {
		logger.severe( message ); 
	}
	
}
