package italo.sisbanco.components.operacoes.pendentes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import italo.sisbanco.ext.openfeign.FeignClientsTestConfiguration;
import italo.sisbanco.ext.rabbitmq.RabbitMQTestConfiguration;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.components.builder.AlteraValorEmContaCacheBuilder;
import italo.sisbanco.kernel.components.operacoes.pendentes.altervaloremconta.AlterCreditoOperacaoPendente;
import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;

@SpringBootTest(classes = SisbancoKernelApplication.class)
@Import({RabbitMQTestConfiguration.class, FeignClientsTestConfiguration.class})
public class AlterCreditoOperacaoPendenteTest extends AbstractOperacaoPendenteTest {	
	
	@Autowired
	private AlterCreditoOperacaoPendente alterCreditoOperacaoPendente;
	
	@Autowired
	private OperAlteraValorEmContaCacheRepository alteraValorEmContaCacheRepository;
	
	private final double creditoInicial = 100;
	private final double novoCreditoValor = 500;

	@Override
	protected void configuraContaRegistro( Conta conta ) {
		conta.setCredito( creditoInicial );
	}

	@Override
	protected OperacaoPendenteResponse registraOperacaoEExecuta( Long contaId ) throws ErrorException {
		AlteraValorEmContaCache alterValor = AlteraValorEmContaCacheBuilder.builder()
				.contaId( contaId )
				.dataOperacao( new Date() )
				.valor( novoCreditoValor )
				.tipo( AlteraValorEmContaTipo.CREDITO )				
				.get();
		
		alteraValorEmContaCacheRepository.save( alterValor );
		
		Optional<AlteraValorEmContaCache> alterValorOp = alteraValorEmContaCacheRepository.findById( alterValor.getId() );
		assertTrue( alterValorOp.isPresent(), "Deveria ter sido encontrada a operação." );

		alterValor = alterValorOp.get();
								
		return alterCreditoOperacaoPendente.executa( alterValor );
	}

	@Override
	protected void assertOpResponse(OperacaoPendenteResponse resp, Conta conta) {
		assertEquals( resp.getConta().getCredito(), novoCreditoValor, "Os valores de crédito não correspondem." );
		assertEquals( resp.getSaldoAnterior(), conta.getSaldo(), "Saldos não correspondem." );		
		
		assertEquals( resp.getOperacaoTipo(), OperacaoPendenteTipo.ALTER_VALOR_EM_CONTA, "Tipos de operação não correspondem." );
		assertEquals( resp.getAlteraValorEmContaTipo(), AlteraValorEmContaTipo.CREDITO, "Tipos de alteração de valor em conta não correspondem." );
		assertTrue( resp.getTransacaoTipo() == null, "Tipo de transação deveria ser nulo." );
	}
	
}
