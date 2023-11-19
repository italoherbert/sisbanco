package italo.sisbanco.shared;

public class AutorizacaoException extends SistemaException {

	private static final long serialVersionUID = 1L;

	public AutorizacaoException(String msg, String[] params) {
		super(msg, params);
	}

}
