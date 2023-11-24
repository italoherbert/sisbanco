package italo.sisbanco.security;

import italo.sisbanco.exception.SistemaException;

public class AuthorizatorException extends SistemaException {

	private static final long serialVersionUID = 1L;

	public AuthorizatorException(String erroChave, Object... erroParams) {
		super( erroChave, erroParams );
	}

}
