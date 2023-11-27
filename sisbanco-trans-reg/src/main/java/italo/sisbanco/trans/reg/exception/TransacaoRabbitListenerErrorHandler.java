package italo.sisbanco.trans.reg.exception;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

public class TransacaoRabbitListenerErrorHandler implements RabbitListenerErrorHandler {
			
	private Logger logger;
	
	public TransacaoRabbitListenerErrorHandler( String errorLogFile ) {
		File file = new File( errorLogFile );
		System.out.println( file.exists()+"  "+errorLogFile );
		if (!file.exists() )
			if ( file.getParentFile() != null )
				file.getParentFile().mkdirs();
		
		FileHandler fileHandler = null;
		try {			
			fileHandler = new FileHandler( errorLogFile );
			fileHandler.setFormatter( new SimpleFormatter() );						
		} catch ( IOException e ) {
			throw new RuntimeException( "Não foi possível tratar o arquivo de log: "+errorLogFile );
		}		
		
		logger = Logger.getLogger( "queue-transacoes" );
		logger.addHandler( fileHandler );
	}
	
	@Override
	public Object handleError(
			org.springframework.amqp.core.Message amqpMessage, 
			org.springframework.messaging.Message<?> message,
			ListenerExecutionFailedException exception) throws Exception {
		logger.log( Level.SEVERE, "Mensagem em formato inválido. Mensagem= "+new String( amqpMessage.getBody() ) );
		return null;
	}

}
