package italo.sisbanco.kernel.exception;

public class ErrorException extends Exception {

	private static final long serialVersionUID = 1L;

	private String errorChave;	
	private Object[] errorParams;
	
	private int status;
	private String messageBody;
	
	public ErrorException( int status, String messageBody ) {
		this.status = status;
		this.messageBody = messageBody;
	}
	
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
	
	public int getStatus() {
		return status;
	}
	
	public String getMessageBody() {
		return messageBody;
	}
	
}