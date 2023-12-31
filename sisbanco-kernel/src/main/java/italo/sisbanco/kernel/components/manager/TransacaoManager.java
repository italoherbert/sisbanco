package italo.sisbanco.kernel.components.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.messageria.TransacaoMessageSender;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.repository.ContaRepository;
import jakarta.transaction.Transactional;

@Component
public class TransacaoManager {

	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private TransacaoMessageSender transacaoMessageSender;
					
	public void credita( Conta conta, double valor ) throws ErrorException {				
		conta.setSaldo( conta.getSaldo() + valor );
		contaRepository.save( conta );

		transacaoMessageSender.envia( conta, valor, TransacaoTipo.CREDITO );
	}
	
	public void debita( Conta conta, double valor ) throws ErrorException {				
		if ( conta.getSaldo()-valor < -conta.getCredito() )
			throw new ErrorException( Erros.CREDITO_INSUFICIENTE );
			
		conta.setSaldo( conta.getSaldo() - valor ); 
		contaRepository.save( conta );
		
		transacaoMessageSender.envia( conta, valor, TransacaoTipo.DEBITO );
	}
	
	@Transactional
	public void transfere( Conta origem, Conta dest, double valor ) throws ErrorException {				
		if ( origem.getSaldo()-valor < -origem.getCredito() )
			throw new ErrorException( Erros.CREDITO_INSUFICIENTE );
		
		origem.setSaldo( origem.getSaldo() - valor );
		dest.setSaldo( dest.getSaldo() + valor ); 
		
		contaRepository.save( origem );
		contaRepository.save( dest );		
		
		transacaoMessageSender.envia( origem, valor, TransacaoTipo.TRANSFERENCIA );
	}
	
}
