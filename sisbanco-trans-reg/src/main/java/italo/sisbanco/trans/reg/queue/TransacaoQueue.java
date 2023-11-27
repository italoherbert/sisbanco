package italo.sisbanco.trans.reg.queue;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import italo.sisbanco.trans.reg.model.TransacaoQueueMessage;
import italo.sisbanco.trans.reg.service.TransacaoService;
import jakarta.validation.Valid;

@Component
public class TransacaoQueue {

	@Autowired
	private TransacaoService transacaoService;
				
	@RabbitListener(
			queues = {"${config.rabbitmq.queue}"}, 
			errorHandler = "transacaoRabbitListenerErrorHandler")
	public void recebeTransacao( @Valid @Payload TransacaoQueueMessage message ) {
		transacaoService.registraTransacao( message );		
	}
	
}
