package italo.sisbanco.kernel.components.operacoes.pendentes.altervaloremconta;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.components.manager.ContaAlterManager;
import italo.sisbanco.kernel.components.operacoes.pendentes.OperacaoPendente;
import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.builder.response.OperacaoPendenteResponseBuilder;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.mapper.ContaMapper;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;

@Component
public class AlterDebitoSimplesLimiteOperacaoPendente implements OperacaoPendente<AlteraValorEmContaCache> {

	@Autowired
	private ContaAlterManager contaAlterManager;

	@Autowired
	private ContaMapper contaMapper;
	
	@Override
	public OperacaoPendenteResponse executa( AlteraValorEmContaCache alterValorCache ) throws ServiceException {				
		long contaId = alterValorCache.getContaId();
		double valor = alterValorCache.getValor();
		
		Conta conta = contaAlterManager.alteraDebitoSimplesLimite( contaId, valor ); 					
		
		double saldo = conta.getSaldo();
		
		return OperacaoPendenteResponseBuilder.builder()
				.conta( conta, contaMapper )
				.saldoAnterior( saldo )
				.alterValorEmContaTipo( AlteraValorEmContaTipo.DEBITO_SIMPLES_LIMITE )
				.dataOperacao( new Date() ) 
				.realizada( true )
				.get();			
	}
	
	
}
