package italo.sisbanco.shared.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerBuilder {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
	
	private Logger logger;
	private FileHandler fileHandler;
	
	public Logger build( String loggerName, String errorLogFile ) {
		logger = Logger.getLogger( loggerName );

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
		
		return logger;
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
