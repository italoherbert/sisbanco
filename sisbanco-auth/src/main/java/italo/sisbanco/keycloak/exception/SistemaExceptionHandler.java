package italo.sisbanco.keycloak.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import italo.sisbanco.shared.exception.SistemaException;
import italo.sisbanco.shared.model.ErroResponse;

@ControllerAdvice
public class SistemaExceptionHandler {

	@ExceptionHandler(SistemaException.class)
	public ResponseEntity<ErroResponse> handleSistemaException( SistemaException ex ) {
		return ResponseEntity.status( 401 ).body( new ErroResponse( ex ) );
	}
	
}
