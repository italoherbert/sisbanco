package italo.sisbanco.kernel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;

@Service
public class TransacaoCacheService {

	@Autowired
	private OperTransacaoCacheRepository transacaoCacheRepository;
	
	public TransacaoCache get( String transacaoId ) throws ServiceException {
		Optional<TransacaoCache> tcacheOp = transacaoCacheRepository.findById( transacaoId );
		if ( !tcacheOp.isPresent() )
			throw new ServiceException( Erros.OPER_TRANSACAO_NAO_ENCONTRADA_EM_CACHE );
		
		TransacaoCache tcache = tcacheOp.get();		
		return tcache;
	}
	
	public List<TransacaoCache> listaPorConta( Long contaId ) throws ServiceException {
		List<TransacaoCache> transacoes = transacaoCacheRepository.findByContaId( contaId );
		return transacoes;
	}
	
}
