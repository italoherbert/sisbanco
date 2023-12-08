package italo.sisbanco.kernel.exception;

import java.util.Iterator;
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

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.model.response.ErroResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(ErrorException.class)
	public ResponseEntity<Object> trataErrorException( ErrorException e ) {
		if ( e.getMessageBody() != null ) {
			return ResponseEntity.status( e.getStatus() ).body( e.getMessageBody() );
		} else {		
			int status = ( e instanceof AuthorizatorException ? 403 : 400 );
			try {
				String message = messageSource.getMessage( e.getErrorChave(), e.getErrorParams(), Locale.getDefault() );
				return ResponseEntity.status( status ).body( new ErroResponse( message ) );
			} catch ( NoSuchMessageException e2 ) {
				return ResponseEntity.status( status ).body( new ErroResponse( e2.getMessage() ) );
			}
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
		
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> trataConstraintViolationException( ConstraintViolationException e ) {
		Iterator<ConstraintViolation<?>> it = e.getConstraintViolations().iterator();
		
		String msg = "Erro de validação.";
		if ( it.hasNext() )
			msg = it.next().getMessage();
		
		return ResponseEntity.status( 400 ).body( new ErroResponse( msg ) );
	}	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> trataMensagemErroException( MethodArgumentNotValidException e ) {						
		BindingResult result = e.getBindingResult();
		String mensagem = result.getFieldError().getDefaultMessage();
		return ResponseEntity.status( 400 ).body( new ErroResponse( mensagem ) );
	}
	
}
