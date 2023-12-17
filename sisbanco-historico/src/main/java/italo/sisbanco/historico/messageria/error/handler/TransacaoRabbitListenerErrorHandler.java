package italo.sisbanco.historico.messageria.error.handler;

import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

import italo.sisbanco.historico.log.GlobalLogger;

public class TransacaoRabbitListenerErrorHandler implements RabbitListenerErrorHandler {
			
	private final GlobalLogger globalLogger;
	
	public TransacaoRabbitListenerErrorHandler( GlobalLogger globalLogger ) {
		this.globalLogger = globalLogger;
	}
	
	@Override
	public Object handleError(
			org.springframework.amqp.core.Message amqpMessage, 
			org.springframework.messaging.Message<?> message,
			ListenerExecutionFailedException exception) throws Exception {

		String body = new String( amqpMessage.getBody() );
		globalLogger.severe( "Mensagem em formato inválido. Mensagem= "+body );
		return null;
	}

}
