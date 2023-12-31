package italo.sisbanco.historico.messageria;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import italo.sisbanco.historico.exception.ErrorException;
import italo.sisbanco.historico.model.message.TransacaoMessage;
import italo.sisbanco.historico.service.TransacaoService;
import jakarta.validation.Valid;

@Component
public class TransacaoMesseger {

	@Autowired
	private TransacaoService transacaoService;
				
	@RabbitListener(
			queues = {"${config.rabbitmq.transacoes.queue}"}, 
			errorHandler = "transacaoRabbitListenerErrorHandler")
	public void recebeTransacao( @Valid @Payload TransacaoMessage message ) throws ErrorException {
		transacaoService.registraTransacao( message );		
	}
	
}
