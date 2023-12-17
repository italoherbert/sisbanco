package italo.sisbanco.ext.rabbitmq;

import static org.mockito.Mockito.mock;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import italo.sisbanco.kernel.messageria.TransacaoMessageSender;

@TestConfiguration
public class MockedRabbitMQTestConfiguration {

	@Bean
	TransacaoMessageSender transacaoMessageSender() {
		return mock( TransacaoMessageSender.class );
	}
	
	@Bean
	Queue transacoesQueue() {
		return mock( Queue.class );
	}
	
	@Bean
	Exchange transacoesExchange() {
		return mock( Exchange.class );
	}
	
	@Bean
	Binding transacoesBinding() {
		return mock( Binding.class );
	}
	
}
