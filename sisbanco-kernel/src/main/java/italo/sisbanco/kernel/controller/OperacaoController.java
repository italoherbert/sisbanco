package italo.sisbanco.kernel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.kernel.apidoc.banco.DepositarEndpoint;
import italo.sisbanco.kernel.apidoc.banco.SacarEndpoint;
import italo.sisbanco.kernel.apidoc.banco.TransferirEndpoint;
import italo.sisbanco.kernel.exception.ErrorException;
import italo.sisbanco.kernel.model.request.conta.ValorRequest;
import italo.sisbanco.kernel.model.response.conta.OperacaoPendenteResponse;
import italo.sisbanco.kernel.security.Authorizator;
import italo.sisbanco.kernel.service.OperacaoService;

@RestController
@RequestMapping("/api/kernel/operacoes")
public class OperacaoController {
	
	@Autowired
	private OperacaoService operacaoService;
	
	@Autowired
	private Authorizator authorizator;
			
	@DepositarEndpoint
	@PreAuthorize("hasAuthority('contaDonoWRITE')")
	@PostMapping("/contas/{contaId}/depositar")
	public ResponseEntity<Object> depositar( 
			@PathVariable Long contaId, 
			@RequestBody ValorRequest request ) throws ErrorException {
		
		authorizator.ownerAuthorize( contaId );
		
		OperacaoPendenteResponse resp = operacaoService.credita( contaId, request );
		return ResponseEntity.ok( resp );		
	}
	
	@SacarEndpoint
	@PreAuthorize("hasAuthority('contaDonoWRITE')")
	@PostMapping("/contas/{contaId}/sacar")
	public ResponseEntity<Object> sacar( 
			@PathVariable Long contaId, 
			@RequestBody ValorRequest request ) throws ErrorException {
		
		authorizator.ownerAuthorize( contaId );

		OperacaoPendenteResponse resp = operacaoService.debita( contaId, request );
		return ResponseEntity.ok( resp );		
	}
	
	@TransferirEndpoint
	@PreAuthorize("hasAuthority('contaDonoWRITE')")
	@PostMapping("/contas/orig/{origContaId}/dest/{destContaId}/transferir")
	public ResponseEntity<Object> transferir( 
			@PathVariable Long origContaId,
			@PathVariable Long destContaId,
			@RequestBody ValorRequest request ) throws ErrorException {
		
		authorizator.ownerAuthorize( origContaId );
		
		OperacaoPendenteResponse resp = operacaoService.transfere( origContaId, destContaId, request );
		return ResponseEntity.ok( resp );		
	}
	
}
