package italo.sisbanco.messageria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import italo.sisbanco.ext.log.MainConfiguration;
import italo.sisbanco.ext.openfeign.MockedFeignClientsTestConfiguration;
import italo.sisbanco.ext.rabbitmq.RabbitMQTest;
import italo.sisbanco.ext.redis.MockedRedisTestConfiguration;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.messageria.TransacaoMessageSender;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.message.TransacaoMessage;

@SpringBootTest(classes = SisbancoKernelApplication.class)
@Import({
	MainConfiguration.class, 
	MockedFeignClientsTestConfiguration.class,
	MockedRedisTestConfiguration.class
})
@TestPropertySource(properties = {
	"config.rabbitmq.transacoes.queue=transacoes-queue",	
	"config.rabbitmq.transacoes.exchange=transacoes-exchange",
	"config.rabbitmq.transacoes.routing-key=transacoes-routing-key"
})
public class TransacaoMessageSenderTest extends RabbitMQTest {

	@Value("${config.rabbitmq.transacoes.queue}")
	private String queueName;
	
	@Autowired
	private TransacaoMessageSender transacaoMessageSender;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
				
	@Test
	public void testSend() {
		String username = "italo";
		double valor = 1000;
		TransacaoTipo tipo = TransacaoTipo.CREDITO;
		String tipoStr = tipo.name();
		
		Conta conta = new Conta();
		conta.setUsername( username );
		
		transacaoMessageSender.envia( conta, valor, tipo );	
		
		TransacaoMessage message = (TransacaoMessage)rabbitTemplate.receiveAndConvert( queueName );
		assertNotNull( message, "A mensagem recebida é nula." );
		assertEquals( conta.getUsername(), message.getUsername(), "Usernames não correspondem." );
		assertEquals( message.getUsername(), username, "Usernames não correspondem." );
		assertEquals( message.getValor(), valor, "Valores não correspondem." );
		assertEquals( message.getTipo(), tipoStr, "Tipos não correspondem." );
	}
	
}
