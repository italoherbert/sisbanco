package italo.sisbanco.kernel.components.manager;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.repository.ContaRepository;

@Component
public class ContaAlterManager {
	
	@Autowired
	private ContaRepository contaRepository;
	
	public Conta alteraSaldo( Long contaId, double valor ) throws ErrorException {
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		conta.setSaldo( valor );
		contaRepository.save( conta );
		
		return conta;
	}
	
	public Conta alteraCredito( Long contaId, double valor ) throws ErrorException {
		if ( valor < 0 )
			throw new ErrorException( Erros.CREDITO_NEGATIVO );
		
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_NAO_ENCONTRADA );
				
		Conta conta = contaOp.get();
		conta.setCredito( valor );
		contaRepository.save( conta );	
		
		return conta;
	}
	
	public Conta alteraLimiteOperacao( Long contaId, double valor ) throws ErrorException {
		if ( valor < 0 )
			throw new ErrorException( Erros.LIMITE_OPERACAO_NEGATIVO );
		
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		conta.setLimiteOperacao( valor );
		contaRepository.save( conta );		
		
		return conta;
	}
		
}
