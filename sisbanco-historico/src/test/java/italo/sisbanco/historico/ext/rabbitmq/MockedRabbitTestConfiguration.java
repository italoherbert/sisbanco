
package italo.sisbanco.historico.ext.rabbitmq;

import static org.mockito.Mockito.mock;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class MockedRabbitTestConfiguration {
    		
	@Bean
    @Primary
    ConnectionFactory connectionFactory() {
        return mock( CachingConnectionFactory.class );
    }
	
}
