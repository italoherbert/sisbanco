package italo.sisbanco.historico.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import italo.sisbanco.historico.model.response.ErroResponse;

@ControllerAdvice
public class GlobalMessageHandler {

	@Autowired
	private MessageSourceAdapter messageSourceAdapter;
				
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> trataAccessDeniedException( AccessDeniedException e ) {
		String message = messageSourceAdapter.getMessage( Erros.ACESSO_NAO_AUTORIZADO );
		return ResponseEntity.status( 403 ).body( new ErroResponse( message ) );		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> trataMensagemErroException( MethodArgumentNotValidException e ) {						
		BindingResult result = e.getBindingResult();
		String mensagem = result.getFieldError().getDefaultMessage();
		return ResponseEntity.status( 400 ).body( new ErroResponse( mensagem ) );
	}
	
	
}
