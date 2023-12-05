package italo.sisbanco.kernel.service.operacoes.pendentes.transacao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
public class DebitoOperacaoPendente implements OperacaoPendente{
	
	@Autowired
	private OperTransacaoCacheRepository operTransacaoCacheRepository;
	
	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private TransacaoManager transacaoManager;
	
	@Autowired
	private ContaMapper contaMapper;
	
	@Override
	public OperacaoPendenteResponse executa( String operacaoPendenteId ) throws ServiceException {
		Optional<TransacaoCache> transacaoCacheOp = 
				operTransacaoCacheRepository.findByOperacaoPendente( operacaoPendenteId, TransacaoTipo.DEBITO );
		
		OperacaoPendenteResponse resp = new OperacaoPendenteResponse();

		if ( transacaoCacheOp.isPresent() ) {					
			TransacaoCache transacaoCache = transacaoCacheOp.get();
			String transacaoCacheId = transacaoCache.getId();
			
			Long contaId = transacaoCache.getOrigContaId();
			double valor = transacaoCache.getValor();
				
			Optional<Conta> contaOp = contaRepository.findById( contaId );
			if ( !contaOp.isPresent() )
				throw new ServiceException( Erros.CONTA_NAO_ENCONTRADA );
			
			Conta conta = contaOp.get();
									
			transacaoManager.debita( conta, valor );
			operTransacaoCacheRepository.deleteById( transacaoCacheId );
			
			double saldo = conta.getSaldo();
			
			ContaResponse contaResp = contaMapper.novoContaResponse();
			contaMapper.carregaResponse( contaResp, conta ); 
			
			resp.setRealizada( true );		
			resp.setSaldoAnterior( saldo );
			resp.setSaldoAtual( saldo - valor );
			resp.setConta( contaResp );
			resp.setOperacaoTipo( OperacaoPendenteTipo.TRANSACAO ); 
			resp.setTransacaoTipo( transacaoCache.getTipo() );		
			resp.setValorEmContaTipo( null );
		} else {
			resp.setRealizada( false ); 
		}
		return resp;
	}
	
}
