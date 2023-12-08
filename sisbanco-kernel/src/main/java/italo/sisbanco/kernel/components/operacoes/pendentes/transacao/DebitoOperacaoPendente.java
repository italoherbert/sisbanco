package italo.sisbanco.kernel.components.operacoes.pendentes.transacao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.components.builder.response.OperacaoPendenteResponseBuilder;
import italo.sisbanco.kernel.components.manager.TransacaoManager;
import italo.sisbanco.kernel.components.mapper.ContaMapper;
import italo.sisbanco.kernel.components.operacoes.pendentes.OperacaoPendente;
import italo.sisbanco.kernel.enums.TransacaoTipo;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.ContaRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;

@Component
public class DebitoOperacaoPendente implements OperacaoPendente<TransacaoCache> {
	
	@Autowired
	private OperTransacaoCacheRepository operTransacaoCacheRepository;
	
	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private TransacaoManager transacaoManager;
	
	@Autowired
	private ContaMapper contaMapper;
		
	@Override
	public OperacaoPendenteResponse executa( TransacaoCache transacaoCache ) throws ErrorException {		
		String transacaoCacheId = transacaoCache.getId();
		
		Long contaId = transacaoCache.getOrigContaId();
		double valor = transacaoCache.getValor();
			
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_NAO_ENCONTRADA );
		
		Conta conta = contaOp.get();
		
		double saldoAnterior = conta.getSaldo();

		transacaoManager.debita( conta, valor );
		operTransacaoCacheRepository.deleteById( transacaoCacheId );
				
		return OperacaoPendenteResponseBuilder.builder()
			.conta( conta, contaMapper )
			.valor( valor ) 
			.saldoAnterior( saldoAnterior )
			.dataCriacao( transacaoCache.getDataCriacao() )
			.transacaoTipo( TransacaoTipo.DEBITO )
			.realizada( true )
			.get();	
	}
	
}
