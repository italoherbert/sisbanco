package italo.sisbanco.exception;

public class SistemaException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String msg;
	private String[] params;
	
	public SistemaException( String msg, String... params ) {
		this.msg = msg;		
		this.params = params;	
	}
	
	public String mensagem() {
		String mensagem = msg;
		
		int i = 1;
		for( String p : params ) {
			mensagem = mensagem.replace( "$"+i, p );
			i++;
		}	
		
		return mensagem;
	}
	
}
