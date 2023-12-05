package italo.sisbanco.kernel.service.manager;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.repository.ContaRepository;

@Component
public class ContaAlterManager {
	
	@Autowired
	private ContaRepository contaRepository;

	public Conta alteraSaldo( Long contaId, double valor ) throws ServiceException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		conta.setSaldo( valor );
		contaRepository.save( conta );
		
		return conta;
	}
	
	public Conta alteraCredito( Long contaId, double valor ) throws ServiceException {
		if ( valor < 0 )
			throw new ServiceException( Erros.CREDITO_NEGATIVO );
		
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
				
		Conta conta = contaOp.get();
		conta.setCredito( valor );
		contaRepository.save( conta );	
		
		return conta;
	}
	
	public Conta alteraDebitoSimplesLimite( Long contaId, double valor ) throws ServiceException {
		if ( valor < 0 )
			throw new ServiceException( Erros.DEBITO_SIMPLES_LIMITE_NEGATIVO );
		
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		conta.setDebitoSimplesLimite( valor );
		contaRepository.save( conta );		
		
		return conta;
	}
	
}
