package italo.sisbanco.historico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.historico.apidoc.transacao.FiltraTransacoesEndpoint;
import italo.sisbanco.historico.apidoc.transacao.ListaUltimasTransacoesEndpoint;
import italo.sisbanco.historico.model.Transacao;
import italo.sisbanco.historico.model.request.TransacaoFiltroRequest;
import italo.sisbanco.historico.service.TransacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/historico/transacoes")
public class TransacaoController {

	@Autowired
	private TransacaoService transacaoService;
			
	@ListaUltimasTransacoesEndpoint
	@PreAuthorize("hasAuthority('historicoTransacoesREAD')")
	@GetMapping("/lista/ultimas/{quant}")
	public ResponseEntity<Object> lista( @PathVariable int quant ) {		
		List<Transacao> transacoes = transacaoService.listaOrderDesc( quant ); 
		return ResponseEntity.ok( transacoes );
	}
	
	@FiltraTransacoesEndpoint
	@PreAuthorize("hasAuthority('historicoTransacoesREAD')")
	@PostMapping("/filtra")
	public ResponseEntity<Object> filtra( @Valid @RequestBody TransacaoFiltroRequest request ) {
		List<Transacao> transacoes = transacaoService.filtra( request );  
		return ResponseEntity.ok( transacoes );
	}
	
}
