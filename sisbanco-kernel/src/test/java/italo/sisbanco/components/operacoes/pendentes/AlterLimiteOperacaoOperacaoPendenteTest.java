package italo.sisbanco.components.operacoes.pendentes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import italo.sisbanco.ext.log.MainConfiguration;
import italo.sisbanco.ext.openfeign.MockedFeignClientsTestConfiguration;
import italo.sisbanco.ext.rabbitmq.MockedRabbitMQTestConfiguration;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.components.builder.AlteraValorEmContaCacheBuilder;
import italo.sisbanco.kernel.components.operacoes.pendentes.altervaloremconta.AlterLimiteOperacaoOperacaoPendente;
import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;

@SpringBootTest(classes = SisbancoKernelApplication.class)
@Import({
	MainConfiguration.class, 
	MockedRabbitMQTestConfiguration.class, 
	MockedFeignClientsTestConfiguration.class
})
public class AlterLimiteOperacaoOperacaoPendenteTest extends AbstractOperacaoPendenteTest {	
	
	@Autowired
	private AlterLimiteOperacaoOperacaoPendente alterDebitoSimplesLimiteOperacaoPendente;
	
	@Autowired
	private OperAlteraValorEmContaCacheRepository alteraValorEmContaCacheRepository;
	
	private final double limiteOperacaoInicial = 500;
	private final double novoLimiteOperacaoValor = 1000;

	@Override
	protected void configuraContaRegistro( Conta conta ) {
		conta.setLimiteOperacao( limiteOperacaoInicial );
	}

	@Override
	protected OperacaoPendenteResponse registraOperacaoEExecuta( Long contaId ) throws ErrorException {
		AlteraValorEmContaCache alterValor = AlteraValorEmContaCacheBuilder.builder()
				.contaId( contaId )
				.valor( novoLimiteOperacaoValor )
				.tipo( AlteraValorEmContaTipo.LIMITE_OPERACAO )				
				.get();
		
		alteraValorEmContaCacheRepository.save( alterValor );
		
		Optional<AlteraValorEmContaCache> alterValorOp = alteraValorEmContaCacheRepository.findById( alterValor.getId() );
		assertTrue( alterValorOp.isPresent(), "Deveria ter sido encontrada a operação." );

		alterValor = alterValorOp.get();
								
		return alterDebitoSimplesLimiteOperacaoPendente.executa( alterValor );
	}

	@Override
	protected void assertOpResponse(OperacaoPendenteResponse resp, Conta conta) {
		assertEquals( resp.getConta().getLimiteOperacao(), novoLimiteOperacaoValor, "Os valores de limite de operação não correspondem." );
		assertEquals( resp.getSaldoAnterior(), conta.getSaldo(), "Saldos não correspondem." );		
		assertEquals( resp.getValor(), resp.getConta().getLimiteOperacao(), "Limites de operação não correspondem." );
		
		assertEquals( resp.getOperacaoTipo(), OperacaoPendenteTipo.ALTER_VALOR_EM_CONTA, "Tipos de operação não correspondem." );
		assertEquals( resp.getAlteraValorEmContaTipo(), AlteraValorEmContaTipo.LIMITE_OPERACAO, "Tipos de alteração de valor em conta não correspondem." );
		assertTrue( resp.getTransacaoTipo() == null, "Tipo de transação deveria ser nulo." );
	}
	
}