package italo.sisbanco.components.operacoes.pendentes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import italo.sisbanco.ext.log.MainConfiguration;
import italo.sisbanco.ext.openfeign.FeignClientsTestConfiguration;
import italo.sisbanco.ext.rabbitmq.RabbitMQTestConfiguration;
import italo.sisbanco.kernel.SisbancoKernelApplication;
import italo.sisbanco.kernel.components.builder.TransacaoCacheBuilder;
import italo.sisbanco.kernel.components.operacoes.pendentes.transacao.TransferenciaOperacaoPendente;
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
	RabbitMQTestConfiguration.class, 
	FeignClientsTestConfiguration.class
})
public class TransferenciaOperacaoPendenteTest extends AbstractOperacaoPendenteTest {	
	
	@Autowired
	private TransferenciaOperacaoPendente transferenciaOperacaoPendente;
	
	@Autowired
	private OperTransacaoCacheRepository transacaoCacheRepository;
		
	private final double transferenciaValor = 100;
	private final double saldo = 500;
	private final double debitoLimite = 1000;
	private final long contaDestinoId = 4;
	
	@Override
	protected void configuraContaRegistro( Conta conta ) {
		conta.setSaldo( saldo );
		conta.setDebitoSimplesLimite( debitoLimite );
	}

	@Override
	protected OperacaoPendenteResponse registraOperacaoEExecuta( Long contaId ) throws ErrorException {
		TransacaoCache tc = TransacaoCacheBuilder.builder()
				.contaOrigemId( contaId )
				.contaDestinoId( contaDestinoId )
				.dataOperacao( new Date() )
				.valor( transferenciaValor )
				.tipo( TransacaoTipo.TRANSFERENCIA )
				.get();
		
		transacaoCacheRepository.save( tc );
		
		Optional<TransacaoCache> tcOp = transacaoCacheRepository.findById( tc.getId() );
		assertTrue( tcOp.isPresent(), "Deveria ter sido encontrada a transação." );

		tc = tcOp.get();
								
		return transferenciaOperacaoPendente.executa( tc );
	}

	@Override
	protected void assertOpResponse(OperacaoPendenteResponse resp, Conta conta) {
		assertEquals( resp.getValor(), transferenciaValor, "Valores de transferencia não correspondem." );
		assertEquals( resp.getSaldoAnterior(), conta.getSaldo() + transferenciaValor, "Saldos não correspondem." );		

		assertEquals( resp.getOperacaoTipo(), OperacaoPendenteTipo.TRANSACAO, "Tipos de operação não correspondem." );
		assertEquals( resp.getTransacaoTipo(), TransacaoTipo.TRANSFERENCIA, "Tipos de transação não correspondem." );
		assertTrue( resp.getAlteraValorEmContaTipo() == null, "Tipo de alteração em conta deveria ser nulo." ); 
	}

}
