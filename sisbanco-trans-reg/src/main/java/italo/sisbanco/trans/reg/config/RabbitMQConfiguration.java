package italo.sisbanco.trans.reg.config;

import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import italo.sisbanco.trans.reg.exception.TransacaoRabbitListenerErrorHandler;

@Configuration
public class RabbitMQConfiguration {	
	
	@Value("${config.message.queue.logging.error.file}")
	private String transacoesErrorLogFile;
	
	@Bean("transacaoRabbitListenerErrorHandler") 
	RabbitListenerErrorHandler transacaoRabbitListenerErrorHandler() {
		return new TransacaoRabbitListenerErrorHandler( transacoesErrorLogFile );
	}
			
}
