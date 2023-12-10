package italo.sisbanco.auth.exception;

public class ErrorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String errorChave;
	private Object[] errorParams;
		
	public ErrorException( String errorChave, Object... errorParams ) {
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