package italo.sisbanco.trans.reg.model.response;

public class ErroResponse {

	private String mensagem;

	public ErroResponse() {}
		
	public ErroResponse( String mensagem ) {
		this.mensagem = mensagem;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem( String mensagem ) {
		this.mensagem = mensagem;
	}
	
}
