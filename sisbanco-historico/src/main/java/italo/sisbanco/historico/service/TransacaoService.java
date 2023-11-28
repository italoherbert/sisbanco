package italo.sisbanco.historico.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import italo.sisbanco.historico.model.Transacao;
import italo.sisbanco.historico.model.message.TransacaoMessage;
import italo.sisbanco.historico.model.request.TransacaoFiltroRequest;
import italo.sisbanco.historico.repository.TransacaoRepository;

@Service
public class TransacaoService {
	
	@Autowired
	private TransacaoRepository transacaoRepository;
		
	public void registraTransacao( TransacaoMessage request ) {
		Transacao transacao = new Transacao();
		transacao.setUsername( request.getUsername() );
		transacao.setValor( request.getValor() );
		transacao.setDataOperacao( request.getDataOperacao() );
		transacao.setTipo( request.getTipo() );
		
		transacaoRepository.save( transacao );
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
	
}
