package italo.sisbanco.kernel.service.operacoes.pendentes.transacao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.exception.ServiceException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.enums.OperacaoPendenteTipo;
import italo.sisbanco.kernel.model.enums.TransacaoTipo;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.ContaRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;
import italo.sisbanco.kernel.service.manager.TransacaoManager;
import italo.sisbanco.kernel.service.mapper.ContaMapper;
import italo.sisbanco.kernel.service.operacoes.pendentes.OperacaoPendente;

public class TransferenciaOperacaoPendente implements OperacaoPendente {

	@Autowired
	private OperTransacaoCacheRepository operTransacaoCacheRepository;
	
	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private TransacaoManager transacaoManager;
	
	@Autowired
	private ContaMapper contaMapper;
	
	@Override
	public OperacaoPendenteResponse executa(String operacaoPendenteId) throws ServiceException {
		Optional<TransacaoCache> tcacheOp = 
				operTransacaoCacheRepository.findByOperacaoPendente( operacaoPendenteId, TransacaoTipo.TRANSFERENCIA );
		
		OperacaoPendenteResponse resp = new OperacaoPendenteResponse();
		if ( tcacheOp.isPresent() ) {					
			TransacaoCache tcache = tcacheOp.get();
			String transacaoCacheId = tcache.getId();
			
			Long origemContaId = tcache.getOrigContaId();
			Long destContaId = tcache.getDestContaId();
			double valor = tcache.getValor();
			
			Optional<Conta> origemContaOp = contaRepository.findById( origemContaId );
			if ( !origemContaOp.isPresent() )
				throw new ServiceException( Erros.CONTA_ORIGEM_NAO_ENCONTRADA );
			
			Optional<Conta> destContaOp = contaRepository.findById( destContaId );
			if ( !destContaOp.isPresent() )
				throw new ServiceException( Erros.CONTA_DEST_NAO_ENCONTRADA );
					
			Conta origem = origemContaOp.get();
			Conta dest = destContaOp.get();
					
			transacaoManager.transfere( origem, dest, valor );
			operTransacaoCacheRepository.deleteById( transacaoCacheId );
			
			
			double saldo = origem.getSaldo();
			
			ContaResponse contaResp = contaMapper.novoContaResponse();
			contaMapper.carregaResponse( contaResp, origem ); 
			
			resp.setRealizada( true );		
			resp.setSaldoAnterior( saldo );
			resp.setSaldoAtual( saldo - valor );
			resp.setConta( contaResp );
			resp.setOperacaoTipo( OperacaoPendenteTipo.TRANSACAO ); 
			resp.setTransacaoTipo( tcache.getTipo() );		
			resp.setValorEmContaTipo( null ); 
		} else {
			resp.setRealizada( false );
		}
		
		return resp;
	}

}
