package italo.sisbanco.kernel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.kernel.apidoc.banco.DepositarEndpoint;
import italo.sisbanco.kernel.apidoc.banco.ExecutaTransacaoCacheEndpoint;
import italo.sisbanco.kernel.apidoc.banco.SacarEndpoint;
import italo.sisbanco.kernel.apidoc.banco.TransferirEndpoint;
import italo.sisbanco.kernel.exception.SistemaException;
import italo.sisbanco.kernel.model.request.conta.ValorRequest;
import italo.sisbanco.kernel.model.response.conta.TransacaoResponse;
import italo.sisbanco.kernel.service.BancoService;

@RestController
@RequestMapping("/api/kernel/banco")
public class BancoController {
	
	@Autowired
	private BancoService bancoService;
	
	@ExecutaTransacaoCacheEndpoint
	@PreAuthorize("hasAuthority('cacheTransacoesALL')")
	@GetMapping("/exec/transacao/{transacaoId}")
	public ResponseEntity<Object> executaTransacaoCache( 
			@PathVariable String transacaoId ) throws SistemaException {
		
		TransacaoResponse resp = bancoService.executaTransacaoCache( transacaoId );
		return ResponseEntity.ok( resp );
	}
	
	@DepositarEndpoint
	@PreAuthorize("hasAuthority('contaDonoWRITE')")
	@PostMapping("/depositar/{contaId}")
	public ResponseEntity<Object> depositar( 
			@PathVariable Long contaId, 
			@RequestBody ValorRequest request ) throws SistemaException {
		
		TransacaoResponse resp = bancoService.credita( contaId, request );
		return ResponseEntity.ok( resp );		
	}
	
	@SacarEndpoint
	@PreAuthorize("hasAuthority('contaDonoWRITE')")
	@PostMapping("/sacar/{contaId}")
	public ResponseEntity<Object> sacar( 
			@PathVariable Long contaId, 
			@RequestBody ValorRequest request ) throws SistemaException {
		
		TransacaoResponse resp = bancoService.debita( contaId, request );
		return ResponseEntity.ok( resp );		
	}
	
	@TransferirEndpoint
	@PreAuthorize("hasAuthority('contaDonoWRITE')")
	@PostMapping("/transferir/orig/{origContaId}/dest/{destContaId}")
	public ResponseEntity<Object> transferir( 
			@PathVariable Long origContaId,
			@PathVariable Long destContaId,
			@RequestBody ValorRequest request ) throws SistemaException {
		
		TransacaoResponse resp = bancoService.transfere( origContaId, destContaId, request );
		return ResponseEntity.ok( resp );		
	}
	
}
