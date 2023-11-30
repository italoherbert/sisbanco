package italo.sisbanco.kernel.security;

import italo.sisbanco.kernel.exception.SistemaException;

public class AuthorizatorException extends SistemaException {

	private static final long serialVersionUID = 1L;

	public AuthorizatorException(String erroChave, Object... erroParams) {
		super( erroChave, erroParams );
	}

}
