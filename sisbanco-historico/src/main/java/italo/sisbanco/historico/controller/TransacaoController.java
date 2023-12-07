package italo.sisbanco.historico.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.historico.apidoc.transacao.FiltraTransacoesEndpoint;
import italo.sisbanco.historico.apidoc.transacao.ListaUltimasTransacoesEndpoint;
import italo.sisbanco.historico.model.Transacao;
import italo.sisbanco.historico.model.request.TransacaoFiltroRequest;
import italo.sisbanco.historico.service.TransacaoService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/historico/transacoes")
public class TransacaoController {

	@Autowired
	private TransacaoService transacaoService;
			
	@ListaUltimasTransacoesEndpoint
	@PreAuthorize("hasAuthority('historicoTransacoesREAD')")
	@GetMapping("/ultimas/{quant}/lista")
	public ResponseEntity<Object> lista( @PathVariable int quant ) {		
		List<Transacao> transacoes = transacaoService.listaOrderDesc( quant ); 
		return ResponseEntity.ok( transacoes );
	}
	
	@FiltraTransacoesEndpoint
	@PreAuthorize("hasAuthority('historicoTransacoesREAD')")
	@GetMapping
	public ResponseEntity<Object> filtra( 
			@NotBlank( message="{username.obrigatorio}" ) @RequestParam String username, 
			@NotNull( message = "{dataInicio.obrigatoria}") @RequestParam Date dataInicio,
			@NotNull( message = "{dataFim.obrigatoria}") @RequestParam Date dataFim) {
		
		TransacaoFiltroRequest request = new TransacaoFiltroRequest();
		request.setUsername( username );
		request.setDataInicio( dataInicio );
		request.setDataFim( dataFim ); 
		
		List<Transacao> transacoes = transacaoService.filtra( request );  
		return ResponseEntity.ok( transacoes );
	}
	
}
