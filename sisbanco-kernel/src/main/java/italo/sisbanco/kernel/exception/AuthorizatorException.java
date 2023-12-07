package italo.sisbanco.kernel.exception;

public class AuthorizatorException extends Exception {

	private static final long serialVersionUID = 1L;

	private String errorChave;
	private Object[] errorParams;
	
	public AuthorizatorException( String errorChave, Object... errorParams ) {
		this.errorChave = errorChave;
		this.errorParams = errorParams;
	}
	
	public String getErrorChave() {
		return errorChave;
	}
	
	public Object[] getErrorParams() {
		return errorParams;
	}
	
}
