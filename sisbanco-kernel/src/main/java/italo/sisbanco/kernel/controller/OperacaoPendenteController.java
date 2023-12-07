package italo.sisbanco.kernel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.kernel.apidoc.banco.ExecutaOperacaoPendenteCacheEndpoint;
import italo.sisbanco.kernel.apidoc.transacao.cache.GetTransacaoEmCachePorIDEndpoint;
import italo.sisbanco.kernel.apidoc.transacao.cache.ListaTransacoesEmCacheEndpoint;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.service.OperacaoPendenteCacheService;

@RestController
@RequestMapping("/api/kernel/operacoes/pendentes")
public class OperacaoPendenteController {

	@Autowired
	private OperacaoPendenteCacheService operacaoPendenteCacheService;
		
	@ExecutaOperacaoPendenteCacheEndpoint
	@PreAuthorize("hasAuthority('cacheOperacoesPendentesALL')")
	@GetMapping("/{operacaoPendenteId}/exec")
	public ResponseEntity<Object> executaTransacaoCache( 
			@PathVariable String operacaoPendenteId ) throws ErrorException {
		
		OperacaoPendenteResponse resp = operacaoPendenteCacheService.executa( operacaoPendenteId );
		return ResponseEntity.ok( resp );
	}
	
	@GetTransacaoEmCachePorIDEndpoint
	@PreAuthorize("hasAuthority('cacheOperacoesPendentesALL')")
	@GetMapping("/{operacaoPendenteId}")
	public ResponseEntity<Object> get( @PathVariable String operacaoPendenteId ) throws ErrorException {
		OperacaoPendenteResponse resp = operacaoPendenteCacheService.get( operacaoPendenteId );
		return ResponseEntity.ok( resp );
	}
	
	@ListaTransacoesEmCacheEndpoint
	@PreAuthorize("hasAuthority('cacheOperacoesPendentesALL')")
	@GetMapping("/contas/{contaId}/lista")
	public ResponseEntity<Object> listaPorConta( @PathVariable Long contaId ) throws ErrorException {
		List<OperacaoPendenteResponse> lista = operacaoPendenteCacheService.listaPorConta( contaId );
		return ResponseEntity.ok( lista );
	}			
		
}
