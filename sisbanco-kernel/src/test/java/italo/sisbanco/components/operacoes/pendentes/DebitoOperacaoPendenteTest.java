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
import italo.sisbanco.kernel.components.builder.TransacaoCacheBuilder;
import italo.sisbanco.kernel.components.operacoes.pendentes.transacao.DebitoOperacaoPendente;
import italo.sisbanco.kernel.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;

@SpringBootTest(classes = SisbancoKernelApplication.class)
@Import({
	MainConfiguration.class, 
	MockedRabbitMQTestConfiguration.class, 
	MockedFeignClientsTestConfiguration.class
})
public class DebitoOperacaoPendenteTest extends AbstractOperacaoPendenteTest {	
	
	@Autowired
	private DebitoOperacaoPendente debitoOperacaoPendente;
	
	@Autowired
	private OperTransacaoCacheRepository transacaoCacheRepository;
	
	private final double debitoValor = 100;
	private final double saldo = 500;
	private final double limiteOperacao = 1000;
	
	@Override
	protected void configuraContaRegistro( Conta conta ) {
		conta.setSaldo( saldo );
		conta.setLimiteOperacao( limiteOperacao );
	}

	@Override
	protected OperacaoPendenteResponse registraOperacaoEExecuta( Long contaId ) throws ErrorException {
		TransacaoCache tc = TransacaoCacheBuilder.builder()
				.contaOrigemId( contaId )
				.contaDestinoId( 0 )
				.valor( debitoValor )
				.tipo( TransacaoTipo.TRANSFERENCIA )
				.get();
		
		transacaoCacheRepository.save( tc );
		
		Optional<TransacaoCache> tcOp = transacaoCacheRepository.findById( tc.getId() );
		assertTrue( tcOp.isPresent(), "Deveria ter sido encontrada a transação." );

		tc = tcOp.get();
								
		return debitoOperacaoPendente.executa( tc );
	}

	@Override
	protected void assertOpResponse(OperacaoPendenteResponse resp, Conta conta) {
		assertEquals( resp.getValor(), debitoValor, "Valores de débito não correspondem." );
		assertEquals( resp.getSaldoAnterior(), conta.getSaldo() + debitoValor, "Saldos não correspondem." );		

		assertEquals( resp.getOperacaoTipo(), OperacaoPendenteTipo.TRANSACAO, "Tipos de operação não correspondem." );
		assertEquals( resp.getTransacaoTipo(), TransacaoTipo.DEBITO, "Tipos de transação não correspondem." );
		assertTrue( resp.getAlteraValorEmContaTipo() == null, "Tipo de alteração em conta deveria ser nulo." ); 
	}		
	
}
