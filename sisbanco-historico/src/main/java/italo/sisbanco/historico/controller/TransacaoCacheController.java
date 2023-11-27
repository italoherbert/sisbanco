package italo.sisbanco.historico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.historico.cache.TransacaoCache;
import italo.sisbanco.historico.model.CacheTransacao;

@RestController
@RequestMapping("/api/cache/transacoes")
public class TransacaoCacheController {

	@Autowired
	private TransacaoCache transacaoCache;
	
	@PutMapping("/put")
	public ResponseEntity<Object> seta( @RequestBody CacheTransacao request ) {
		transacaoCache.setTransacao( request );
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/get/{transacaoId}")
	public ResponseEntity<Object> get( @PathVariable Long transacaoId ) {
		CacheTransacao resp = transacaoCache.getTransacao( transacaoId );
		return ResponseEntity.ok( resp );
	}
	
	@DeleteMapping("/deleta/{transacaoId}")
	public ResponseEntity<Object> deleta( @PathVariable Long transacaoId ) {
		transacaoCache.deletaTransacao( transacaoId );
		return ResponseEntity.ok().build();
	}
	
}
