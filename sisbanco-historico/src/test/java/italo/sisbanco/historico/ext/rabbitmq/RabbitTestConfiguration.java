
package italo.sisbanco.historico.ext.rabbitmq;

import org.mockito.Mockito;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitTestConfiguration {
    
	@Bean
    @Primary
    ConnectionFactory connectionFactory() {
        return Mockito.mock(CachingConnectionFactory.class);
    }
	
}
