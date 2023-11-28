package italo.sisbanco.historico.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import italo.sisbanco.historico.exception.TransacaoRabbitListenerErrorHandler;

@Configuration
public class RabbitMQConfiguration {	
	
	@Value("${config.message.logging.error.file}")
	private String transacoesErrorLogFile;
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Bean("transacaoRabbitListenerErrorHandler") 
	RabbitListenerErrorHandler transacaoRabbitListenerErrorHandler() {
		return new TransacaoRabbitListenerErrorHandler( transacoesErrorLogFile );
	}
	
	@Bean
	MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
			
	@Bean
	SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory( connectionFactory ); 
		factory.setMessageConverter( jsonMessageConverter() ); 
		return factory;
	}
	
}
