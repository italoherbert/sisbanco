package italo.sisbanco.kernel.service.operacoes.pendentes.altervaloremconta;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.enums.ValorEmContaTipo;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.service.manager.ContaAlterManager;
import italo.sisbanco.kernel.service.mapper.ContaMapper;
import italo.sisbanco.kernel.service.operacoes.pendentes.OperacaoPendente;

@Component
public class AlterCreditoOperacaoPendente implements OperacaoPendente {

	@Autowired
	private OperAlteraValorEmContaCacheRepository operAlterValorRepository;
	
	@Autowired
	private ContaAlterManager contaAlterManager;
	
	@Autowired
	private ContaMapper contaMapper;

	@Override
	public OperacaoPendenteResponse executa(String operacaoPendenteId) throws ServiceException {
		Optional<AlteraValorEmContaCache> alterValorCacheOp = 
				operAlterValorRepository.findByOperacaoPendente( operacaoPendenteId, ValorEmContaTipo.CREDITO );
		
		OperacaoPendenteResponse resp = new OperacaoPendenteResponse();

		if ( alterValorCacheOp.isPresent() ) {
			AlteraValorEmContaCache alterValorCache = alterValorCacheOp.get();
			
			long contaId = alterValorCache.getContaId();
			double valor = alterValorCache.getValor();
			
			Conta conta = contaAlterManager.alteraCredito( contaId, valor );
			
			ContaResponse contaResp = contaMapper.novoContaResponse();
			contaMapper.carregaResponse( contaResp, conta );
			
			
		} else {
			
		}
		return resp;
	}
	
	
}
