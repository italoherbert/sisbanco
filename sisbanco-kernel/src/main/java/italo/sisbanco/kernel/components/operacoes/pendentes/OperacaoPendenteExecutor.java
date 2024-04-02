package italo.sisbanco.kernel.components.operacoes.pendentes;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.components.operacoes.pendentes.altervaloremconta.AlterCreditoOperacaoPendente;
import italo.sisbanco.kernel.components.operacoes.pendentes.altervaloremconta.AlterLimiteOperacaoOperacaoPendente;
import italo.sisbanco.kernel.components.operacoes.pendentes.transacao.DebitoOperacaoPendente;
import italo.sisbanco.kernel.components.operacoes.pendentes.transacao.TransferenciaOperacaoPendente;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.repository.OperAlteraValorEmContaCacheRepository;
import italo.sisbanco.kernel.repository.OperTransacaoCacheRepository;

@Component
public class OperacaoPendenteExecutor {

	@Autowired
	private OperTransacaoCacheRepository transacaoCacheRepository;
	
	@Autowired
	private OperAlteraValorEmContaCacheRepository alterValorEmContaCacheRepository;
	
	
	@Autowired
	private DebitoOperacaoPendente debitoOperPendente;
	
	@Autowired
	private TransferenciaOperacaoPendente transferenciaOperacaoPendente;
	
	@Autowired
	private AlterCreditoOperacaoPendente alterCreditoOperacaoPendente;
	
	@Autowired
	private AlterLimiteOperacaoOperacaoPendente alterDebitoSimplesLimiteOperacaoPendente;
			
	public OperacaoPendenteResponse executa( String operacaoPendenteId ) throws ErrorException {
		Optional<TransacaoCache> transacaoCacheOp = transacaoCacheRepository.findByOperacaoPendenteId( operacaoPendenteId );
		
		if ( transacaoCacheOp.isPresent() ) {
			TransacaoCache transacaoCache = transacaoCacheOp.get();
			switch ( transacaoCache.getTipo() ) {
				case DEBITO:
					return debitoOperPendente.executa( transacaoCache );
				case TRANSFERENCIA:
					return transferenciaOperacaoPendente.executa( transacaoCache );
				default:
					throw new ErrorException( Erros.OPER_TRANSACAO_TIPO_INVALIDO );
			}			
		} else {
			Optional<AlteraValorEmContaCache> alterVCacheOp = alterValorEmContaCacheRepository.findByOperacaoPendenteId( operacaoPendenteId );
			
			if ( alterVCacheOp.isPresent() ) {
				AlteraValorEmContaCache alterVCache = alterVCacheOp.get();
				switch( alterVCache.getTipo() ) {
					case CREDITO:
						return alterCreditoOperacaoPendente.executa( alterVCache );
					case LIMITE_OPERACAO:
						return alterDebitoSimplesLimiteOperacaoPendente.executa( alterVCache );						
					default:
						throw new ErrorException( Erros.OPER_ALTER_VALOR_EM_CONTA_TIPO_INVALIDO );
				}
			} else {
				throw new ErrorException( Erros.OPERACAO_PENDENTE_NAO_ENCONTRADA );
			}
		}						
	}		
	
}
