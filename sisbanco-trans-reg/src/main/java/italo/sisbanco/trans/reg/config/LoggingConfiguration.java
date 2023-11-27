package italo.sisbanco.trans.reg.config;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {

	@Value("${config.message.queue.logging.error.file}")
	private String transacoesErrorLogFile;
	
	@Bean("messageQueueLogger")
	Logger messageQueueLogger() {
		FileHandler fileHandler = null;
		try {
			fileHandler = new FileHandler( transacoesErrorLogFile );
			fileHandler.setFormatter( new SimpleFormatter() );						
		} catch ( IOException e ) {
			throw new RuntimeException( "Não foi possível tratar o arquivo de log: "+transacoesErrorLogFile );
		}		
		
		Logger logger = Logger.getLogger( "queue-transacoes" );
		logger.addHandler( fileHandler );
		return logger;
	}
	
	@Bean("cacheLogger")
	Logger cacheLogger() {					
		Logger logger = Logger.getLogger( "cache-transacoes" );
		return logger;
	}
	
}
