package italo.sisbanco.kernel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.kernel.apidoc.transacao.cache.GetTransacaoEmCachePorIDEndpoint;
import italo.sisbanco.kernel.apidoc.transacao.cache.ListaTransacoesEmCacheEndpoint;
import italo.sisbanco.kernel.exception.SistemaException;
import italo.sisbanco.kernel.model.cache.TransacaoCache;
import italo.sisbanco.kernel.service.TransacaoCacheService;

@RestController
@RequestMapping("/api/kernel/cache/transacoes")
public class TransacaoCacheController {

	@Autowired
	private TransacaoCacheService transacaoCacheService;
		
	@GetTransacaoEmCachePorIDEndpoint
	@PreAuthorize("hasAuthority('cacheTransacoesALL')")
	@GetMapping("/get/{transacaoId}")
	public ResponseEntity<Object> get( @PathVariable String transacaoId ) throws SistemaException {
		TransacaoCache resp = transacaoCacheService.get( transacaoId );
		return ResponseEntity.ok( resp );
	}
	
	@ListaTransacoesEmCacheEndpoint
	@PreAuthorize("hasAuthority('cacheTransacoesALL')")
	@GetMapping("/lista/{contaId}")
	public ResponseEntity<Object> listaPorConta( @PathVariable Long contaId ) throws SistemaException {
		List<TransacaoCache> lista = transacaoCacheService.listaPorConta( contaId );
		return ResponseEntity.ok( lista );
	}			
		
}
