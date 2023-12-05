package italo.sisbanco.kernel.service.operacoes.pendentes;

import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;

public interface OperacaoPendente {

	public OperacaoPendenteResponse executa( String operacaoPendenteId ) throws ServiceException;
	
}
