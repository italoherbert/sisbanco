package italo.sisbanco.exception;

import italo.sisbanco.shared.SistemaException;

public class KernelException extends SistemaException {

	private static final long serialVersionUID = 1L;

	public KernelException(String msg, String... params) {
		super(msg, params);
	}
	
}
