package italo.sisbanco.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import italo.sisbanco.shared.AutorizacaoException;
import italo.sisbanco.shared.ErroResponse;

@ControllerAdvice
public class SistemaExceptionHandler {

	@ExceptionHandler(AutorizacaoException.class)
	public ResponseEntity<Object> trataAutorizacaoException( AutorizacaoException e ) {
		return ResponseEntity.status( 401 ).body( new ErroResponse( e ) );
	}
	
	@ExceptionHandler(KernelException.class)
	public ResponseEntity<Object> trataKernelException( KernelException e ) {
		return ResponseEntity.status( 400 ).body( new ErroResponse( e ) );
	}
	
}
