package italo.sisbanco.queue;

import java.util.Date;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.enums.TransacaoTipo;
import italo.sisbanco.model.Conta;
import italo.sisbanco.model.message.TransacaoMessage;

@Component
public class TransacaoQueue {

	@Autowired
	private Binding transacoesBinding;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public void envia( Conta conta, double valor, TransacaoTipo tipo ) {
		TransacaoMessage message = new TransacaoMessage();
		message.setUsername( conta.getUsername() );
		message.setValor( valor );
		message.setDataOperacao( new Date() );
		message.setTipo( tipo.name() );
		
		rabbitTemplate.convertAndSend( 
				transacoesBinding.getExchange(),
				transacoesBinding.getRoutingKey(),
				message );
	}
	
}
