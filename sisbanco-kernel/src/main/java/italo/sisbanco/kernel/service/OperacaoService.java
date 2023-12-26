package italo.sisbanco.kernel.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.components.builder.AlteraValorEmContaCacheBuilder;
import italo.sisbanco.kernel.components.builder.TransacaoCacheBuilder;
import italo.sisbanco.kernel.components.builder.response.OperacaoPendenteResponseBuilder;
import italo.sisbanco.kernel.components.manager.TransacaoManager;
import italo.sisbanco.kernel.components.mapper.ContaMapper;
import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.enums.OperacaoPendenteStatus;
import italo.sisbanco.kernel.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.cache.OperacaoPendenteCache;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.request.conta.ValorRequest;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.ContaRepository;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;

@Service
public class OperacaoService {

	@Autowired
	private TransacaoManager transacaoManagerService;
	
	@Autowired
	private OperTransacaoCacheRepository transacaoCacheRepository;
	
	@Autowired
	private OperAlteraValorEmContaCacheRepository alteraValorEmContaCacheRepository;
		
	@Autowired
	private ContaRepository contaRepository;	
		
	@Autowired
	private ContaMapper contaMapper;
	
	public OperacaoPendenteResponse alteraCredito( Long contaId, ValorRequest request) throws ErrorException {
		return this.alteraValorEmConta( contaId, request.getValor(), AlteraValorEmContaTipo.CREDITO );
	}
	
	public OperacaoPendenteResponse alteraDebitoSimplesLimite( Long contaId, ValorRequest request) throws ErrorException {
		return this.alteraValorEmConta( contaId, request.getValor(), AlteraValorEmContaTipo.DEBITO_SIMPLES_LIMITE );
	}
	
	public OperacaoPendenteResponse alteraValorEmConta( Long contaId, double valor, AlteraValorEmContaTipo tipo ) throws ErrorException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		double saldo = conta.getSaldo();
				
		AlteraValorEmContaCache alterVCache = AlteraValorEmContaCacheBuilder.builder()
				.contaId( contaId )
				.tipo( tipo )
				.valor( valor )				
				.get();
		
		alteraValorEmContaCacheRepository.save( alterVCache );
		
		return OperacaoPendenteResponseBuilder.builder()
				.status( OperacaoPendenteStatus.AGUARDANDO_EXECUCAO )
				.operacaoPendente( alterVCache.getOperacaoPendente() ) 
				.conta( conta, contaMapper )
				.saldoAnterior( saldo )
				.alterValorEmContaTipo( tipo )
				.dataCriacao( alterVCache.getDataCriacao() )
				.get();	
	}
	
	public OperacaoPendenteResponse credita( Long contaId, ValorRequest request ) throws ErrorException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		double saldoAnterior = conta.getSaldo();
		
		double valor = request.getValor();		
		
		transacaoManagerService.credita( conta, valor );
		
		ContaResponse contaResp = contaMapper.novoContaResponse();
		contaMapper.carregaResponse( contaResp, conta );
		
		return OperacaoPendenteResponseBuilder.builder()
				.status( OperacaoPendenteStatus.REALIZADA )
				.operacaoPendente( null ) 
				.conta( conta, contaMapper )
				.valor( valor )
				.saldoAnterior( saldoAnterior )
				.transacaoTipo( TransacaoTipo.CREDITO )
				.dataCriacao( new Date() ) 
				.get();		
	}
	
	public OperacaoPendenteResponse debita( Long contaId, ValorRequest request ) throws ErrorException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		double saldo = conta.getSaldo();
		
		double valor = request.getValor();							
		OperacaoPendenteCache operCache = null;

		OperacaoPendenteStatus status;

		Date dataCriacao = null;
		
		if ( valor > conta.getLimiteOperacao() ) {										
			TransacaoCache tcache = TransacaoCacheBuilder.builder()
					.contaOrigemId( conta.getId() )
					.valor( valor )
					.tipo( TransacaoTipo.DEBITO )
					.get();			 
			
			transacaoCacheRepository.save( tcache );
			
			operCache = tcache.getOperacaoPendente();
			dataCriacao = tcache.getDataCriacao();
			status = OperacaoPendenteStatus.AGUARDANDO_EXECUCAO;
		} else {
			transacaoManagerService.debita( conta, valor );
			
			dataCriacao = new Date();
			status = OperacaoPendenteStatus.REALIZADA;
		}
				
		return OperacaoPendenteResponseBuilder.builder()
				.status( status )
				.operacaoPendente( operCache )
				.conta( conta, contaMapper )
				.valor( valor )
				.saldoAnterior( saldo )
				.transacaoTipo( TransacaoTipo.DEBITO )
				.dataCriacao( dataCriacao )
				.get();
	}
	
	public OperacaoPendenteResponse transfere( Long origemContaId, Long destContaId, ValorRequest request ) throws ErrorException {
		Optional<Conta> origemContaOp = contaRepository.findById( origemContaId );
		if ( !origemContaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_ORIGEM_NAO_ENCONTRADA );
		
		Optional<Conta> destContaOp = contaRepository.findById( destContaId );
		if ( !destContaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_DEST_NAO_ENCONTRADA );
				
		Conta origem = origemContaOp.get();
		Conta dest = destContaOp.get();
		
		double origSaldoAnterior = origem.getSaldo();
		
		double valor = request.getValor();
		
		OperacaoPendenteStatus status;
		Date dataCriacao = null;
		OperacaoPendenteCache operCache = null;
				
		if ( valor > origem.getLimiteOperacao() ) {
			OperacaoPendenteCache oper = new OperacaoPendenteCache();
			oper.setTipo( OperacaoPendenteTipo.TRANSACAO ); 
			
			TransacaoCache tcache = TransacaoCacheBuilder.builder()
					.contaOrigemId( origem.getId() )
					.contaDestinoId( dest.getId() )
					.valor( valor )
					.tipo( TransacaoTipo.TRANSFERENCIA )
					.get();			
			
			transacaoCacheRepository.save( tcache );
			
			status = OperacaoPendenteStatus.AGUARDANDO_EXECUCAO;
			dataCriacao = tcache.getDataCriacao();
			operCache = tcache.getOperacaoPendente();
		} else {
			transacaoManagerService.transfere( origem, dest, valor );
			
			dataCriacao = new Date();
			status = OperacaoPendenteStatus.REALIZADA;
		}	
		
		return OperacaoPendenteResponseBuilder.builder()
				.status( status )
				.operacaoPendente( operCache ) 
				.conta( origem, contaMapper )
				.valor( valor )
 				.transacaoTipo( TransacaoTipo.TRANSFERENCIA )
				.dataCriacao( dataCriacao )
				.saldoAnterior( origSaldoAnterior ) 				
				.get();
	}
	
}
