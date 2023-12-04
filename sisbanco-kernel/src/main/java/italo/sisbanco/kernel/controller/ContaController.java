package italo.sisbanco.kernel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.kernel.apidoc.conta.AlteraContaEndpoint;
import italo.sisbanco.kernel.apidoc.conta.DeletaContaEndpoint;
import italo.sisbanco.kernel.apidoc.conta.FiltraContaEndpoint;
import italo.sisbanco.kernel.apidoc.conta.GetContaPorIDEndpoint;
import italo.sisbanco.kernel.apidoc.conta.RegistraContaEndpoint;
import italo.sisbanco.kernel.exception.SistemaException;
import italo.sisbanco.kernel.model.request.conta.ContaFiltroRequest;
import italo.sisbanco.kernel.model.request.conta.ContaSaveRequest;
import italo.sisbanco.kernel.model.response.conta.ContaResponse;
import italo.sisbanco.kernel.service.ContaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/kernel/conta")
public class ContaController {
		
	@Autowired
	private ContaService contaService;
	
	@RegistraContaEndpoint
	@PreAuthorize("hasAuthority('contaWRITE')")
	@PostMapping("/registra")
	public ResponseEntity<Object> registra( 
			@Valid @RequestBody ContaSaveRequest request, 
			HttpServletRequest httpRequest ) throws SistemaException {
			
		String authorizationHeader = httpRequest.getHeader( "Authorization" );
		ContaResponse resp = contaService.registra( request, authorizationHeader ); 
		return ResponseEntity.ok( resp );
	}
	
	@AlteraContaEndpoint
	@PreAuthorize("hasAnyAuthority('contaWRITE', 'contaDonoWRITE')")
	@PutMapping("/altera/{contaId}")	
	public ResponseEntity<Object> altera(
			@PathVariable Long contaId, 
			@Valid @RequestBody ContaSaveRequest request ) throws SistemaException {
		contaService.altera( contaId, request );
		return ResponseEntity.ok().build();
	}
	
	@GetContaPorIDEndpoint
	@PreAuthorize("hasAuthority('contaREAD')")
	@GetMapping("/get/{contaId}")
	public ResponseEntity<Object> get(
			@PathVariable Long contaId ) throws SistemaException {
		ContaResponse resp = contaService.get( contaId );
		return ResponseEntity.ok( resp );
	}
	
	@FiltraContaEndpoint	
	@PreAuthorize("hasAuthority('contaREAD')")
	@PostMapping("/filtra")
	public ResponseEntity<Object> filtra(
			@Valid @RequestBody ContaFiltroRequest request ) throws SistemaException {
		List<ContaResponse> resp = contaService.filtra( request );
		return ResponseEntity.ok( resp );
	}
	
	@DeletaContaEndpoint
	@PreAuthorize("hasAnyAuthority('contaDELETE', 'contaDonoDELETE')")
	@DeleteMapping("/deleta/{contaId}")
	public ResponseEntity<Object> deleta(
			@PathVariable Long contaId,
			HttpServletRequest httpRequest ) throws SistemaException {
		
		String authorizationHeader = httpRequest.getHeader( "Authorization" );
		contaService.deleta( contaId, authorizationHeader );		
		return ResponseEntity.ok().build();
	}
	
}
