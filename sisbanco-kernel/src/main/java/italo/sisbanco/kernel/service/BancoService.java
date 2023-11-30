package italo.sisbanco.kernel.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.enums.TransacaoTipo;
import italo.sisbanco.kernel.model.request.conta.ValorRequest;
import italo.sisbanco.kernel.model.response.conta.TransacaoResponse;
import italo.sisbanco.kernel.repository.ContaRepository;
import italo.sisbanco.kernel.repository.TransacaoCacheRepository;
import italo.sisbanco.kernel.service.manager.TransacaoManagerService;

@Service
public class BancoService {

	@Autowired
	private TransacaoManagerService transacaoManagerService;
	
	@Autowired
	private TransacaoCacheRepository transacaoCacheRepository;
	
	@Autowired
	private ContaRepository contaRepository;
	
	public TransacaoResponse executaTransacaoCache( String transacaoId ) throws ServiceException {
		Optional<TransacaoCache> tcacheOp = transacaoCacheRepository.findById( transacaoId );
		if ( !tcacheOp.isPresent() )
			throw new ServiceException( Erros.TRANSACAO_NAO_ENCONTRADA_EM_CACHE );
		
		TransacaoCache tcache = tcacheOp.get();
		
		TransacaoResponse resp = new TransacaoResponse();
		resp.setTipo( tcache.getTipo() );
		
		if( tcache.getTipo() == TransacaoTipo.DEBITO ) {
			Long contaId = tcache.getOrigContaId();
			double valor = tcache.getValor();
				
			Optional<Conta> contaOp = contaRepository.findById( contaId );
			if ( !contaOp.isPresent() )
				throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
			
			Conta conta = contaOp.get();
			
			double saldo = conta.getSaldo();
			
			transacaoManagerService.debita( conta, valor );
			transacaoCacheRepository.deleteById( transacaoId );

			resp.setSaldoAnterior( saldo );
			resp.setSaldoAtual( saldo - valor );
			resp.setRealizada( true );
			return resp;
		} else if ( tcache.getTipo() == TransacaoTipo.TRANSFERENCIA ) {		
			Long origemContaId = tcache.getOrigContaId();
			Long destContaId = tcache.getDestContaId();
			double valor = tcache.getValor();
			
			Optional<Conta> origemContaOp = contaRepository.findById( origemContaId );
			if ( !origemContaOp.isPresent() )
				throw new ServiceException( Erros.CONTA_ORIGEM_NAO_ENCONTRADA );
			
			Optional<Conta> destContaOp = contaRepository.findById( destContaId );
			if ( !destContaOp.isPresent() )
				throw new ServiceException( Erros.CONTA_DEST_NAO_ENCONTRADA );
					
			Conta origem = origemContaOp.get();
			Conta dest = destContaOp.get();
			
			double saldo = origem.getSaldo();
			
			transacaoManagerService.transfere( origem, dest, valor );
			transacaoCacheRepository.deleteById( transacaoId ); 
			
			resp.setSaldoAnterior( saldo );
			resp.setSaldoAtual( saldo - valor );
			resp.setRealizada( true );
			return resp;
		} else {			
			throw new ServiceException( Erros.TRANSACAO_TIPO_INVALIDO, tcache.getTipo() );
		}		
	}
	
	public TransacaoResponse credita( Long contaId, ValorRequest request ) throws ServiceException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		double saldo = conta.getSaldo();
		
		double valor = request.getValor();
		
		transacaoManagerService.credita( conta, valor );
		
		TransacaoResponse resp = new TransacaoResponse();
		resp.setSaldoAnterior( saldo );
		resp.setSaldoAtual( saldo + valor ); 
		resp.setRealizada( true );
		resp.setTipo( TransacaoTipo.CREDITO );		
		return resp;
	}
	
	public TransacaoResponse debita( Long contaId, ValorRequest request ) throws ServiceException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		double saldo = conta.getSaldo();
		
		double valor = request.getValor();
		
		TransacaoResponse resp = new TransacaoResponse();
		resp.setSaldoAnterior( conta.getSaldo() ); 
		resp.setTipo( TransacaoTipo.DEBITO ); 
		
		if ( valor > conta.getSemAutorizacaoDebitoLimite() ) {
			TransacaoCache tcache = new TransacaoCache();
			tcache.setOrigContaId( conta.getId() );
			tcache.setValor( valor );
			tcache.setTipo( TransacaoTipo.DEBITO );
			tcache.setDataOperacao( new Date() );
			
			transacaoCacheRepository.save( tcache );
			
			resp.setSaldoAtual( saldo );
			resp.setRealizada( false );
		} else {
			transacaoManagerService.debita( conta, valor );
			
			resp.setSaldoAtual( saldo - valor );
			resp.setRealizada( true );
		}
		
		return resp;
	}
	
	public TransacaoResponse transfere( Long origemContaId, Long destContaId, ValorRequest request ) throws ServiceException {
		Optional<Conta> origemContaOp = contaRepository.findById( origemContaId );
		if ( !origemContaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_ORIGEM_NAO_ENCONTRADA );
		
		Optional<Conta> destContaOp = contaRepository.findById( destContaId );
		if ( !destContaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_DEST_NAO_ENCONTRADA );
				
		Conta origem = origemContaOp.get();
		Conta dest = destContaOp.get();
		
		double origSaldo = origem.getSaldo();
		
		double valor = request.getValor();
		
		TransacaoResponse resp = new TransacaoResponse();		
		resp.setSaldoAnterior( origSaldo );
		resp.setTipo( TransacaoTipo.TRANSFERENCIA ); 
		
		if ( valor > origem.getSemAutorizacaoDebitoLimite() ) {
			TransacaoCache tcache = new TransacaoCache();
			tcache.setOrigContaId( origem.getId() );
			tcache.setDestContaId( dest.getId() ); 
			tcache.setValor( valor );
			tcache.setTipo( TransacaoTipo.TRANSFERENCIA );
			tcache.setDataOperacao( new Date() );
			
			transacaoCacheRepository.save( tcache ); 
			
			resp.setSaldoAtual( origSaldo ); 
			resp.setRealizada( false );
		} else {
			transacaoManagerService.transfere( origem, dest, valor );
			
			resp.setSaldoAtual( origSaldo - valor ); 
			resp.setRealizada( true );
		}
		
		return resp;
	}
	
}
