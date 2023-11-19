package italo.sisbanco.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.sisbanco.Erros;
import italo.sisbanco.exception.ServiceException;
import italo.sisbanco.model.Conta;
import italo.sisbanco.model.request.ValorRequest;
import italo.sisbanco.repository.ContaRepository;
import jakarta.transaction.Transactional;

@Service
public class BancoService {

	@Autowired
	private ContaRepository contaRepository;
	
	public void debita( Long contaId, ValorRequest request ) throws ServiceException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		
		if ( conta.getSaldo()-request.getValor() < -conta.getCredito() )
			throw new ServiceException( Erros.CREDITO_INSUFICIENTE );
			
		conta.setSaldo( conta.getSaldo() - request.getValor() ); 
		contaRepository.save( conta );			
	}
	
	public void credita( Long contaId, ValorRequest request ) throws ServiceException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();		
		conta.setSaldo( conta.getSaldo() + request.getValor() );
		contaRepository.save( conta );
	}
	
	@Transactional
	public void transfere( Long origemContaId, Long destContaId, ValorRequest request ) throws ServiceException {
		Optional<Conta> origemContaOp = contaRepository.findById( origemContaId );
		if ( !origemContaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_ORIGEM_NAO_ENCONTRADA );
		
		Optional<Conta> destContaOp = contaRepository.findById( destContaId );
		if ( !destContaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_DEST_NAO_ENCONTRADA );
				
		Conta origem = origemContaOp.get();
		Conta dest = destContaOp.get();
		
		if ( origem.getSaldo()-request.getValor() < -origem.getCredito() )
			throw new ServiceException( Erros.CREDITO_INSUFICIENTE );
		
		origem.setSaldo( origem.getSaldo() - request.getValor() );
		dest.setSaldo( dest.getSaldo() + request.getValor() ); 
		
		contaRepository.save( origem );
		contaRepository.save( dest );		
	}
	
}
