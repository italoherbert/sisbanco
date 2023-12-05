package italo.sisbanco.kernel.service.operacoes.pendentes.altervaloremconta;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.enums.ValorEmContaTipo;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.service.manager.ContaAlterManager;
import italo.sisbanco.kernel.service.operacoes.pendentes.OperacaoPendente;

@Component
public class AlterDebitoSimplesLimiteOperacaoPendente implements OperacaoPendente {

	@Autowired
	private OperAlteraValorEmContaCacheRepository operAlterValorRepository;
	
	@Autowired
	private ContaAlterManager contaAlterManager;

	@Override
	public OperacaoPendenteResponse executa(String operacaoPendenteId) throws ServiceException {
		Optional<AlteraValorEmContaCache> alterValorCacheOp = 
				operAlterValorRepository.findByOperacaoPendente( operacaoPendenteId, ValorEmContaTipo.CREDITO );
		
		OperacaoPendenteResponse resp = new OperacaoPendenteResponse();

		if ( alterValorCacheOp.isPresent() ) {
			AlteraValorEmContaCache alterValorCache = alterValorCacheOp.get();
			
			long contaId = alterValorCache.getContaId();
			double valor = alterValorCache.getValor();
			
			contaAlterManager.alteraDebitoSimplesLimite( contaId, valor ); 					
		}
		
		return resp;
	}
	
	
}
