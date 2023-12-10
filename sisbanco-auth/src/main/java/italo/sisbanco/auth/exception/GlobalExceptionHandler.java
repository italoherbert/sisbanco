package italo.sisbanco.auth.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import italo.sisbanco.auth.Erros;
import italo.sisbanco.auth.model.ErroResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private MessageSource messageSource;
		
	@ExceptionHandler(ErrorException.class)
	public ResponseEntity<Object> trataErrorException( ErrorException e ) {
		try {
			String message = messageSource.getMessage( e.getErrorChave(), e.getErrorParams(), Locale.getDefault() );
			return ResponseEntity.status( 400 ).body( new ErroResponse( message ) );
		} catch ( NoSuchMessageException e2 ) {
			return ResponseEntity.status( 400 ).body( new ErroResponse( e2.getMessage() ) );
		}
	}		
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> trataAccessDeniedException( AccessDeniedException e ) {
		try {
			String message = messageSource.getMessage( Erros.ACESSO_NAO_AUTORIZADO, null, Locale.getDefault() );
			return ResponseEntity.status( 403 ).body( new ErroResponse( message ) );
		} catch ( NoSuchMessageException e2 ) {
			return ResponseEntity.status( 403 ).body( new ErroResponse( e2.getMessage() ) );
		}
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> trataMensagemErroException( MethodArgumentNotValidException e ) {						
		BindingResult result = e.getBindingResult();
		String mensagem = result.getFieldError().getDefaultMessage();
		return ResponseEntity.status( 400 ).body( new ErroResponse( mensagem ) );
	}
	
}
