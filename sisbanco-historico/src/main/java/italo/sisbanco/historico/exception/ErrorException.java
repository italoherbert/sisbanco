package italo.sisbanco.historico.exception;

public class ErrorException extends Exception {

	private static final long serialVersionUID = 1L;

	private String errorCode;	
	private Object[] errorParams;
	
	public ErrorException( String errorCode, Object... errorParams ) {
		this.errorCode = errorCode;
		this.errorParams = errorParams;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public Object[] getErrorParams() {
		return errorParams;
	}	
	
}
