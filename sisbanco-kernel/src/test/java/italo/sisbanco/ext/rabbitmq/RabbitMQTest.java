package italo.sisbanco.ext.rabbitmq;

import java.time.Duration;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@Import(RabbitMQTestConfiguration.class)
public class RabbitMQTest {

	private static RabbitMQContainer rabbitMQContainer;		
	
	static {
		rabbitMQContainer = new RabbitMQContainer( DockerImageName.parse( "rabbitmq:3.12-management" ) )
				.withStartupTimeout( Duration.ofSeconds( 100 ) );
		rabbitMQContainer.start();		
	}
	
	@DynamicPropertySource
	public static void registerRedisProperties(DynamicPropertyRegistry registry) {
	    registry.add( "spring.rabbitmq.host", () -> rabbitMQContainer.getHost() );
	    registry.add( "spring.rabbitmq.port", () -> rabbitMQContainer.getAmqpPort() );
	}
	
}
