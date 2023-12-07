package italo.sisbanco.kernel.components.operacoes.pendentes.transacao;

import java.util.Date;
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
public class TransferenciaOperacaoPendente implements OperacaoPendente<TransacaoCache> {

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
		
		Long origemContaId = transacaoCache.getOrigContaId();
		Long destContaId = transacaoCache.getDestContaId();
		double valor = transacaoCache.getValor();
		
		Optional<Conta> origemContaOp = contaRepository.findById( origemContaId );
		if ( !origemContaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_ORIGEM_NAO_ENCONTRADA );
		
		Optional<Conta> destContaOp = contaRepository.findById( destContaId );
		if ( !destContaOp.isPresent() )
			throw new ErrorException( Erros.CONTA_DEST_NAO_ENCONTRADA );
				
		Conta origem = origemContaOp.get();
		Conta dest = destContaOp.get();
		
		double saldoAnterior = origem.getSaldo();
				
		transacaoManager.transfere( origem, dest, valor );
		operTransacaoCacheRepository.deleteById( transacaoCacheId );
									
		return OperacaoPendenteResponseBuilder.builder()
				.conta( origem, contaMapper )
				.saldoAnterior( saldoAnterior )
				.dataOperacao( new Date() )
				.transacaoTipo( TransacaoTipo.TRANSFERENCIA )
				.realizada( true )
				.get();	
	}

}
