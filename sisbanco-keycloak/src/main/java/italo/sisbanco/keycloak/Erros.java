package italo.sisbanco.keycloak;

public interface Erros {

	public final static String ACESSO_NAO_AUTORIZADO = "acesso.nao.autorizado";

	public final static String TOKEN_INVALIDO = "token.formato.invalido";
	public final static String TOKEN_EXPIRADO = "token.expirado";
	public final static String TOKEN_CHAVE_PUBLICA_INVALIDA = "token.chave.publica.invalida";
	public final static String TOKEN_ASSINATURA_INVALIDA = "token.assinatura.invalida";

	public final static String TOKEN_SOLICITACAO_FALHA = "token.solicitacao.falha";
	
	public final static String USER_REGISTRO_JA_EXISTE = "user.registro.ja.existe";
	public final static String USER_REGISTRO_FALHA = "user.registro.falha";
	
}
