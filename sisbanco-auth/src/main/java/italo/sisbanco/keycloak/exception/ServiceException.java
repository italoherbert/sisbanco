package italo.sisbanco.keycloak.exception;

import italo.sisbanco.shared.exception.SistemaException;

public class ServiceException extends SistemaException {

	private static final long serialVersionUID = 1L;

	public ServiceException( String msg, String... params ) {
		super(msg, params);
	}

}
