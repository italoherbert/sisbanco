package italo.sisbanco.kernel;

public interface Erros {

	public final static String ACESSO_NAO_AUTORIZADO = "acesso.nao.autorizado";
	
	public final static String USER_ID_NULO = "user.id.nulo";

	public final static String TITULAR_JA_EXISTE = "titular.ja.existe";
	public final static String KEYCLOAK_USER_NAO_CRIADO = "keycloak.user.nao.criado";
	
	public final static String CONTA_NAO_ENCONTRADA = "conta.nao.encontrada";
	public final static String CONTA_ORIGEM_NAO_ENCONTRADA = "conta.origem.nao.encontrada";
	public final static String CONTA_DEST_NAO_ENCONTRADA = "conta.destino.nao.encontrada";
	
	public final static String CREDITO_INSUFICIENTE = "conta.credito.insuficiente";
	public final static String SALDO_INSUFICIENTE = "conta.saldo.insuficiente";

	public final static String OPER_TRANSACAO_NAO_ENCONTRADA_EM_CACHE = "oper.transacao.em.cache.nao.encontrada";
	public final static String OPER_ALTER_VALOR_EM_CONTA_NAO_ENCONTRADA_EM_CACHE = "oper.alter.valor.em.conta.em.cache.nao.encontrada";
	
	public final static String OPER_TRANSACAO_TIPO_INVALIDO = "oper.transacao.tipo.invalido";
	public final static String OPER_ALTER_VALOR_EM_CONTA_TIPO_INVALIDO = "oper.alter.valor.em.conta.tipo.invalido";
	
	public final static String OPERACAO_PENDENTE_NAO_ENCONTRADA = "oper.pendente.nao.encontrada";
	
	public final static String CREDITO_NEGATIVO = "credito.negativo";
	public final static String DEBITO_SIMPLES_LIMITE_NEGATIVO = "debito.simples.limite.negativo";
			
}
