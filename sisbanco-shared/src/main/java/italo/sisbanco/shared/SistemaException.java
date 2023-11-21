package italo.sisbanco.shared;

public class SistemaException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String erroChave;
	private Object[] erroParams;
		
	public SistemaException( String erroChave, Object... erroParams ) {
		this.erroChave = erroChave;		
		this.erroParams = erroParams;			
	}

	public String getErroChave() {
		return erroChave;
	}

	public Object[] getErroParams() {
		return erroParams;
	}
	
}
