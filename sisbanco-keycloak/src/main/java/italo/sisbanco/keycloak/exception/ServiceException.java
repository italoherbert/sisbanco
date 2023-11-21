package italo.sisbanco.keycloak.exception;

public class ServiceException extends SistemaException {

	private static final long serialVersionUID = 1L;

	public ServiceException( String msg, Object... params ) {
		super(msg, params);
	}

}
