package italo.sisbanco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.exception.SistemaException;
import italo.sisbanco.model.request.conta.ValorRequest;
import italo.sisbanco.service.BancoService;

@RestController
@RequestMapping("/api/kernel/banco")
public class BancoController {
	
	@Autowired
	private BancoService bancoService;
	
	@PreAuthorize("hasAuthority('contaDonoWRITE')")
	@PostMapping("/depositar/{contaId}")
	public ResponseEntity<Object> depositar( 
			@PathVariable Long contaId, 
			@RequestBody ValorRequest request ) throws SistemaException {
		
		bancoService.credita( contaId, request );
		return ResponseEntity.ok().build();		
	}
	
	@PreAuthorize("hasAuthority('contaDonoWRITE')")
	@PostMapping("/sacar/{contaId}")
	public ResponseEntity<Object> sacar( 
			@PathVariable Long contaId, 
			@RequestBody ValorRequest request ) throws SistemaException {
		
		bancoService.debita( contaId, request );
		return ResponseEntity.ok().build();		
	}
	
	@PreAuthorize("hasAuthority('contaDonoWRITE')")
	@PostMapping("/transferir/orig/{origContaId}/dest/{destContaId}")
	public ResponseEntity<Object> transferir( 
			@PathVariable Long origContaId,
			@PathVariable Long destContaId,
			@RequestBody ValorRequest request ) throws SistemaException {
		
		bancoService.transfere( origContaId, destContaId, request );
		return ResponseEntity.ok().build();		
	}
	
}
