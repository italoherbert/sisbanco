package italo.sisbanco.historico.exception;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

public class TransacaoRabbitListenerErrorHandler implements RabbitListenerErrorHandler {
			
	private final SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
	
	private final Logger logger;
	private FileHandler fileHandler;
	
	public TransacaoRabbitListenerErrorHandler( String errorLogFile ) {
		logger = Logger.getLogger( "queue-transacoes" );

		if ( errorLogFile != null ) {
			File file = new File( errorLogFile );
			if (!file.exists() )
				if ( file.getParentFile() != null )
					file.getParentFile().mkdirs();
			
			try {			
				fileHandler = new FileHandler( errorLogFile, true );
				fileHandler.setFormatter( new LoggerFormatter() );
			} catch ( IOException e ) {
				throw new RuntimeException( "Não foi possível tratar o arquivo de log: "+errorLogFile );
			}		
			
			logger.addHandler( fileHandler );
		}
	}
	
	@Override
	public Object handleError(
			org.springframework.amqp.core.Message amqpMessage, 
			org.springframework.messaging.Message<?> message,
			ListenerExecutionFailedException exception) throws Exception {

		String body = new String( amqpMessage.getBody() );
		logger.severe( "Mensagem em formato inválido. Mensagem= "+body );
		return null;
	}

	class LoggerFormatter extends Formatter {

		@Override
		public String format(LogRecord record) {
			return record.getLevel() + ": (" + 
					dateFormat.format( new Date( record.getMillis() ) ) + ") - " +
					record.getMessage() + "\n";
		}
		
	}
	
}
