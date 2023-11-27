package italo.sisbanco.trans.reg.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceAdapter {

	@Autowired
	private MessageSource messageSource;
	
	public String getMessage( String errorCode, String... errorParams ) {
		try {
			return messageSource.getMessage( errorCode, errorParams, Locale.getDefault() );
		} catch ( NoSuchMessageException e2 ) {
			return e2.getMessage();
		}
	}
	
}
