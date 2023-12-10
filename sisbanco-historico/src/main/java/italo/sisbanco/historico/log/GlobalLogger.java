package italo.sisbanco.historico.log;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import italo.sisbanco.shared.util.LoggerBuilder;

@Component
public class GlobalLogger {

	private final LoggerBuilder loggerBuilder = new LoggerBuilder();
	private Logger logger;
	
	public GlobalLogger( @Value("${config.global.error.log.file:#{null}}") String errorLogFile ) {
		logger = loggerBuilder.build( "global-error-log", errorLogFile );
	}
	
	public void severe( String message ) {
		logger.severe( message ); 
	}
	
}
