package italo.sisbanco.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import italo.sisbanco.model.response.ErroResponse;

@ControllerAdvice
public class SistemaExceptionHandler {

	@Autowired
	private MessageSource messageSource;
		
	@ExceptionHandler(SistemaException.class)
	public ResponseEntity<Object> trataSistemaException( SistemaException e ) {
		try {
			String message = messageSource.getMessage( e.getErroChave(), e.getErroParams(), Locale.getDefault() );
			return ResponseEntity.status( 400 ).body( new ErroResponse( message ) );
		} catch ( NoSuchMessageException e2 ) {
			return ResponseEntity.status( 400 ).body( new ErroResponse( e2.getMessage() ) );
		}
	}		
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> trataMensagemErroException( MethodArgumentNotValidException e ) {						
		BindingResult result = e.getBindingResult();
		String mensagem = result.getFieldError().getDefaultMessage();
		return ResponseEntity.status( 400 ).body( new ErroResponse( mensagem ) );
	}
	
}
