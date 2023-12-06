package italo.sisbanco.kernel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.components.mapper.OperacaoPendenteMapper;
import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.ContaRepository;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;

@Service
public class OperacaoPendenteCacheService {

	@Autowired
	private OperTransacaoCacheRepository transacaoCacheRepository;
	
	@Autowired
	private OperAlteraValorEmContaCacheRepository alteraValorEmContaCacheRepository;
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private OperacaoPendenteMapper operacaoPendenteMapper;
		
	public OperacaoPendenteResponse get( String operacaoPendenteId ) throws ServiceException {
		Optional<TransacaoCache> transacaoCacheOp = transacaoCacheRepository.findByOperacaoPendenteId( operacaoPendenteId );
						
		if ( transacaoCacheOp.isPresent() ) {
			TransacaoCache transacaoCache = transacaoCacheOp.get();
			TransacaoTipo tipo = transacaoCache.getTipo();
			
			long contaId = transacaoCache.getOrigContaId();
			
			Optional<Conta> contaOp = contaRepository.findById( contaId );
			if ( !contaOp.isPresent() )
				throw new ServiceException( Erros.CONTA_ORIGEM_NAO_ENCONTRADA );
			
			Conta conta = contaOp.get();
			
			return operacaoPendenteMapper.transacaoResponse( conta, tipo );
		} else {
			Optional<AlteraValorEmContaCache> alterVCacheOp = alteraValorEmContaCacheRepository.findByOperacaoPendenteId( operacaoPendenteId );
			
			if ( alterVCacheOp.isPresent() ) {
				AlteraValorEmContaCache alterVCache = alterVCacheOp.get();
				AlteraValorEmContaTipo tipo = alterVCache.getTipo();
				
				long contaId = alterVCache.getContaId();
				
				Optional<Conta> contaOp = contaRepository.findById( contaId );
				if ( !contaOp.isPresent() )
					throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
				
				Conta conta = contaOp.get();
								
				return operacaoPendenteMapper.alterValorEmContaResponse( conta, tipo );
			} else {
				throw new ServiceException( Erros.OPERACAO_PENDENTE_NAO_ENCONTRADA );
			}
		}						
	}
	
	public List<OperacaoPendenteResponse> listaPorConta( Long contaId ) throws ServiceException {		
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		
		List<TransacaoCache> transacoes = transacaoCacheRepository.findByContaId( contaId );
		List<AlteraValorEmContaCache> alters = alteraValorEmContaCacheRepository.findByContaId( contaId );
		
		List<OperacaoPendenteResponse> lista = new ArrayList<>();
		
		for( TransacaoCache tc : transacoes ) {
			TransacaoTipo tipo = tc.getTipo();								
			lista.add( operacaoPendenteMapper.transacaoResponse( conta, tipo ) );
		}
		
		for( AlteraValorEmContaCache alterV : alters ) {
			AlteraValorEmContaTipo tipo = alterV.getTipo();			
			lista.add( operacaoPendenteMapper.alterValorEmContaResponse( conta, tipo ) );
		}
			
		return lista;
	}	
	
}
