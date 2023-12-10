package italo.sisbanco.kernel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.components.builder.response.OperacaoPendenteResponseBuilder;
import italo.sisbanco.kernel.components.mapper.ContaMapper;
import italo.sisbanco.kernel.components.operacoes.pendentes.OperacaoPendenteExecutor;
import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.enums.OperacaoPendenteStatus;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.exception.ErrorException;
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
	private OperacaoPendenteExecutor operacaoPendenteExecutor;
	
	@Autowired
	private ContaMapper contaMapper;
	
	public OperacaoPendenteResponse executa( String operacaoPendenteId ) throws ErrorException {
		OperacaoPendenteResponse resp = operacaoPendenteExecutor.executa( operacaoPendenteId );
		if ( resp == null )
			throw new ErrorException( Erros.OPERACAO_PENDENTE_NAO_ENCONTRADA );
		return resp;				
	}
			
	public OperacaoPendenteResponse get( String operacaoPendenteId ) throws ErrorException {
		Optional<TransacaoCache> transacaoCacheOp = transacaoCacheRepository.findByOperacaoPendenteId( operacaoPendenteId );
						
		if ( transacaoCacheOp.isPresent() ) {
			TransacaoCache transacaoCache = transacaoCacheOp.get();
			TransacaoTipo tipo = transacaoCache.getTipo();
			double valor = transacaoCache.getValor();
			
			long contaId = transacaoCache.getOrigContaId();
			
			Optional<Conta> contaOp = contaRepository.findById( contaId );
			if ( !contaOp.isPresent() )
				throw new ErrorException( Erros.CONTA_ORIGEM_NAO_ENCONTRADA );
			
			Conta conta = contaOp.get();
			
			return OperacaoPendenteResponseBuilder.builder()
					.status( OperacaoPendenteStatus.AGUARDANDO_EXECUCAO )
					.operacaoPendente( transacaoCache.getOperacaoPendente() )
					.conta( conta, contaMapper )
					.valor( valor ) 
					.saldoAnterior( conta.getSaldo() )
					.transacaoTipo( tipo )
					.get();										
		} else {
			Optional<AlteraValorEmContaCache> alterVCacheOp = alteraValorEmContaCacheRepository.findByOperacaoPendenteId( operacaoPendenteId );
			
			if ( alterVCacheOp.isPresent() ) {
				AlteraValorEmContaCache alterVCache = alterVCacheOp.get();
				AlteraValorEmContaTipo tipo = alterVCache.getTipo();
				double valor = alterVCache.getValor();
				
				long contaId = alterVCache.getContaId();
				
				Optional<Conta> contaOp = contaRepository.findById( contaId );
				if ( !contaOp.isPresent() )
					throw new ErrorException( Erros.CONTA_NAO_ENCONTRADA );
				
				Conta conta = contaOp.get();

				return OperacaoPendenteResponseBuilder.builder()
						.status( OperacaoPendenteStatus.AGUARDANDO_EXECUCAO )
						.operacaoPendente( alterVCache.getOperacaoPendente() ) 
						.conta( conta, contaMapper )
						.valor( valor ) 
						.saldoAnterior( conta.getSaldo() )
						.alterValorEmContaTipo( tipo )
						.get();	
			} else {
				throw new ErrorException( Erros.OPERACAO_PENDENTE_NAO_ENCONTRADA );
			}
		}						
	}
	
	public List<OperacaoPendenteResponse> listaPorConta( Long contaId ) throws ErrorException {		
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		
		List<TransacaoCache> transacoes = transacaoCacheRepository.findByContaId( contaId );
		List<AlteraValorEmContaCache> alters = alteraValorEmContaCacheRepository.findByContaId( contaId );
		
		List<OperacaoPendenteResponse> lista = new ArrayList<>();
		
		for( TransacaoCache tc : transacoes ) {
			TransacaoTipo tipo = tc.getTipo();	
			double valor = tc.getValor();
			
			lista.add( OperacaoPendenteResponseBuilder.builder()
				.status( OperacaoPendenteStatus.AGUARDANDO_EXECUCAO )					
				.operacaoPendente( tc.getOperacaoPendente() ) 
				.conta( conta, contaMapper )
				.valor( valor ) 
				.saldoAnterior( conta.getSaldo() )
				.transacaoTipo( tipo )
				.get() );
		}
		
		for( AlteraValorEmContaCache alterV : alters ) {
			AlteraValorEmContaTipo tipo = alterV.getTipo();		
			double valor = alterV.getValor();
			
			lista.add( OperacaoPendenteResponseBuilder.builder()
					.status( OperacaoPendenteStatus.AGUARDANDO_EXECUCAO )
					.operacaoPendente( alterV.getOperacaoPendente() ) 
					.conta( conta, contaMapper )
					.valor( valor ) 
					.saldoAnterior( conta.getSaldo() )
					.alterValorEmContaTipo( tipo )
					.get() );
		}
			
		return lista;
	}	
	
	public void deleta( String operacaoPendenteId ) throws ErrorException {
		Optional<TransacaoCache> transacaoCacheOp = transacaoCacheRepository.findByOperacaoPendenteId( operacaoPendenteId );
		if ( transacaoCacheOp.isPresent() ) {
			TransacaoCache transacaoCache = transacaoCacheOp.get();												
			transacaoCacheRepository.deleteById( transacaoCache.getId() );						
		} else {
			Optional<AlteraValorEmContaCache> alterVCacheOp = alteraValorEmContaCacheRepository.findByOperacaoPendenteId( operacaoPendenteId );
			if ( alterVCacheOp.isPresent() ) {
				AlteraValorEmContaCache alterVCache = alterVCacheOp.get();												
				alteraValorEmContaCacheRepository.deleteById( alterVCache.getId() );				
			} else {
				throw new ErrorException( Erros.OPERACAO_PENDENTE_NAO_ENCONTRADA );
			}
		}				
	}
	
}
