package italo.sisbanco.exception;

public class ServiceException extends SistemaException {

	private static final long serialVersionUID = 1L;

	public ServiceException( String erroChave, Object... erroParams ) {
		super( erroChave, erroParams );
	}

	public ServiceException(int status, String messageBody) {
		super( status, messageBody );
	}

}
