package italo.sisbanco.kernel;

public interface Erros {

	public final static String ACESSO_NAO_AUTORIZADO = "acesso.nao.autorizado";

	public final static String TITULAR_JA_EXISTE = "titular.ja.existe";
	public final static String KEYCLOAK_USER_NAO_CRIADO = "keycloak.user.nao.criado";
	
	public final static String CONTA_NAO_ENCONTRADA = "conta.nao.encontrada";
	public final static String CONTA_ORIGEM_NAO_ENCONTRADA = "conta.origem.nao.encontrada";
	public final static String CONTA_DEST_NAO_ENCONTRADA = "conta.destino.nao.encontrada";
	
	public final static String CREDITO_INSUFICIENTE = "conta.credito.insuficiente";
	public final static String SALDO_INSUFICIENTE = "conta.saldo.insuficiente";

	public final static String TRANSACAO_NAO_ENCONTRADA_EM_CACHE = "transacao.em.cache.nao.encontrada";
	public final static String TRANSACAO_TIPO_INVALIDO = "transacao.em.cache.tipo.invalido";
	
	
		
}
