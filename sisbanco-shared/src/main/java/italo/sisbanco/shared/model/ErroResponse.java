package italo.sisbanco.shared.model;

import italo.sisbanco.shared.exception.SistemaException;

public class ErroResponse {

	private String mensagem;

	public ErroResponse() {}
	
	public ErroResponse( SistemaException e ) {
		this.mensagem = e.getErroMensagem();
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem( String mensagem ) {
		this.mensagem = mensagem;
	}
	
}
