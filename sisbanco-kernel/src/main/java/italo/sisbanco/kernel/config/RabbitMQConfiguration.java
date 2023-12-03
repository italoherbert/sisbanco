package italo.sisbanco.kernel.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class RabbitMQConfiguration {

	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Value("${config.rabbitmq.transacoes.queue}")
	private String transacoesQueueValue;
	
	@Value("${config.rabbitmq.transacoes.exchange}") 
	private String transacoesExchangeValue;
	
	@Value("${config.rabbitmq.transacoes.routing-key}") 
	private String transacoesRoutingKey;
	
	@Bean
	RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory( connectionFactory );
		rabbitTemplate.setMessageConverter( rabbitMessageConverter() ); 
		return rabbitTemplate;
	}
	
	@Bean
	MessageConverter rabbitMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean("transacoesQueue")
	Queue transacoesQueue() {
		return new Queue( transacoesQueueValue, true );
	}
	
	@Bean("transacoesExchange")
	Exchange transacoesExchange() {
		return new DirectExchange( transacoesExchangeValue );
	}
	
	@Bean("transacoesBinding")
	Binding transacoesBinding( Queue queue, Exchange exchange ) {
		return BindingBuilder.bind( queue )
				.to( exchange )
				.with( transacoesRoutingKey )
				.noargs();
	}
	
}
