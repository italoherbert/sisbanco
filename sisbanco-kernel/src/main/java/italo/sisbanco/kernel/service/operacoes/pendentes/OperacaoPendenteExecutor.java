package italo.sisbanco.kernel.service.operacoes.pendentes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.service.operacoes.pendentes.transacao.DebitoOperacaoPendente;
import italo.sisbanco.kernel.service.operacoes.pendentes.transacao.TransferenciaOperacaoPendente;

@Component
public class OperacaoPendenteExecutor {

	@Autowired
	private DebitoOperacaoPendente debitoOperPendente;
	
	@Autowired
	private TransferenciaOperacaoPendente transferenciaOperacaoPendente;
		
	public OperacaoPendenteResponse executa( String operacaoPendenteId ) throws ServiceException {				
		OperacaoPendenteResponse resp = debitoOperPendente.executa( operacaoPendenteId );
		if ( !resp.isRealizada() )
			resp = transferenciaOperacaoPendente.executa( operacaoPendenteId );
		
		return resp;		
	}
	
}
