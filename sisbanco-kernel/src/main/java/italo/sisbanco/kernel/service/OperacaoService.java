package italo.sisbanco.kernel.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.components.builder.TransacaoCacheBuilder;
import italo.sisbanco.kernel.components.builder.response.OperacaoPendenteResponseBuilder;
import italo.sisbanco.kernel.components.manager.TransacaoManager;
import italo.sisbanco.kernel.components.mapper.ContaMapper;
import italo.sisbanco.kernel.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.OperacaoPendenteCache;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.request.conta.ValorRequest;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.ContaRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;

@Service
public class OperacaoService {

	@Autowired
	private TransacaoManager transacaoManagerService;
	
	@Autowired
	private OperTransacaoCacheRepository transacaoCacheRepository;
	
	@Autowired
	private ContaRepository contaRepository;	
		
	@Autowired
	private ContaMapper contaMapper;
			
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
				.conta( conta, contaMapper )
				.saldoAnterior( saldoAnterior )
				.transacaoTipo( TransacaoTipo.CREDITO )
				.dataOperacao( new Date() ) 
				.realizada( true )				
				.get();		
	}
	
	public OperacaoPendenteResponse debita( Long contaId, ValorRequest request ) throws ErrorException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		double saldo = conta.getSaldo();
		
		double valor = request.getValor();							
		boolean realizada = false;
		
		Date dataOperacao = null;
		
		if ( valor > conta.getDebitoSimplesLimite() ) {										
			TransacaoCache tcache = TransacaoCacheBuilder.builder()
					.contaOrigemId( conta.getId() )
					.valor( valor )
					.tipo( TransacaoTipo.DEBITO )
					.get();			 
			
			transacaoCacheRepository.save( tcache );
			
			realizada = false;
		} else {
			transacaoManagerService.debita( conta, valor );
			
			dataOperacao = new Date();
			realizada = true;
		}
				
		return OperacaoPendenteResponseBuilder.builder()
				.conta( conta, contaMapper )
				.saldoAnterior( saldo )
				.transacaoTipo( TransacaoTipo.DEBITO )
				.realizada( realizada )
				.dataOperacao( dataOperacao )
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
		
		boolean realizada = false;
		Date dataOperacao = null;
				
		if ( valor > origem.getDebitoSimplesLimite() ) {
			OperacaoPendenteCache oper = new OperacaoPendenteCache();
			oper.setTipo( OperacaoPendenteTipo.TRANSACAO ); 
			
			TransacaoCache tcache = TransacaoCacheBuilder.builder()
					.contaOrigemId( origem.getId() )
					.contaDestinoId( dest.getId() )
					.valor( valor )
					.tipo( TransacaoTipo.TRANSFERENCIA )
					.dataOperacao( new Date() )					
					.get();			
			
			transacaoCacheRepository.save( tcache );
			
			realizada = false;
			dataOperacao = tcache.getDataOperacao();
		} else {
			transacaoManagerService.transfere( origem, dest, valor );
			
			realizada = true;
		}	
		
		return OperacaoPendenteResponseBuilder.builder()
				.conta( origem, contaMapper )
				.transacaoTipo( TransacaoTipo.TRANSFERENCIA )
				.dataOperacao( dataOperacao )
				.saldoAnterior( origSaldoAnterior ) 
				.realizada( realizada )					
				.get();
	}
	
}
