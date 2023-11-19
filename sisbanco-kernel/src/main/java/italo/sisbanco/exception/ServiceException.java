package italo.sisbanco.exception;

public class ServiceException extends KernelException {

	private static final long serialVersionUID = 1L;

	public ServiceException(String msg, String... params) {
		super(msg, params);
	}

}
