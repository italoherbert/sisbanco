package italo.sisbanco.shared.exception;

public class SistemaException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String mensagem;
	private String[] params;
	
	private String erroMensagem;
	
	public SistemaException( String msg, String... params ) {
		this.mensagem = msg;		
		this.params = params;	
		
		erroMensagem = mensagem;		
		for( int i = 0; i < params.length; i++ )
			erroMensagem = erroMensagem.replaceFirst( "\\$"+(i+1), params[ i ] );	
	}
	
	public String getErroMensagem() {
		return erroMensagem;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public String[] getParams() {
		return params;
	}
	
}
