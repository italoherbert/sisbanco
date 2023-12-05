package italo.sisbanco.kernel.components.operacoes.pendentes;

import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;

public interface OperacaoPendente<T extends Object> {

	public OperacaoPendenteResponse executa( T obj ) throws ServiceException;
	
}
