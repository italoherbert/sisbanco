package italo.sisbanco.historico.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import italo.sisbanco.historico.exception.ErrorException;
import italo.sisbanco.historico.exception.Erros;
import italo.sisbanco.historico.model.Transacao;
import italo.sisbanco.historico.model.message.TransacaoMessage;
import italo.sisbanco.historico.model.request.TransacaoFiltroRequest;
import italo.sisbanco.historico.repository.TransacaoRepository;

@Service
public class TransacaoService {
	
	@Autowired
	private TransacaoRepository transacaoRepository;
		
	public Transacao registraTransacao( TransacaoMessage request ) throws ErrorException {
		Transacao transacao = new Transacao();
		transacao.setUsername( request.getUsername() );
		transacao.setValor( request.getValor() );
		transacao.setDataOperacao( request.getDataOperacao() );
		transacao.setTipo( request.getTipo() );
		
		return transacaoRepository.save( transacao );
	}
		
	public List<Transacao> listaOrderDesc( int quant ) {
		Pageable p = PageRequest.of( 0, quant );
		List<Transacao> transacoes = transacaoRepository.listaOrderDesc( p );
		return transacoes;
	}
	
	public List<Transacao> filtra( TransacaoFiltroRequest request ) {
		String username = request.getUsername();
		Date dataIni = request.getDataInicio();
		Date dataFim = request.getDataFim();
				
		List<Transacao> transacoes = transacaoRepository.filtra( username, dataIni, dataFim );
		return transacoes;
	}
	
	public Transacao get( String id ) throws ErrorException {
		Optional<Transacao> transacaoOp = transacaoRepository.findById( id );
		if ( !transacaoOp.isPresent() )
			throw new ErrorException( Erros.TRANSACAO_NAO_ENCONTRADA );
		
		return transacaoOp.get();
	}
	
	public void deleta( String id ) throws ErrorException {
		boolean existe = transacaoRepository.existsById( id );
		if ( !existe )
			throw new ErrorException( Erros.TRANSACAO_NAO_ENCONTRADA );
		
		transacaoRepository.deleteById( id );
	}
	
}
