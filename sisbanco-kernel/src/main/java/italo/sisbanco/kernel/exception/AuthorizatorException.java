package italo.sisbanco.kernel.exception;

public class AuthorizatorException extends ErrorException {

	private static final long serialVersionUID = 1L;
	
	public AuthorizatorException( String errorChave, Object... errorParams ) {
		super( errorChave, errorParams );		
	}
	
}
