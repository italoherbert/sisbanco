package italo.sisbanco.exception;

public class SistemaException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String erroChave;
	private Object[] erroParams;
	
	private String messageBody = null;
	private int status = 0;
		
	public SistemaException( String erroChave, Object... erroParams ) {
		this.erroChave = erroChave;		
		this.erroParams = erroParams;			
	}
	
	public SistemaException( int status, String messageBody ) {
		this.status = status;
		this.messageBody = messageBody;
	}

	public String getErroChave() {
		return erroChave;
	}

	public Object[] getErroParams() {
		return erroParams;
	}

	public String getMessageBody() {
		return messageBody;
	}
	
	public int getStatus() {
		return status;
	}
	
}
