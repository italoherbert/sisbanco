package italo.sisbanco.controller;

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

import italo.sisbanco.exception.SistemaException;
import italo.sisbanco.model.request.conta.ContaFiltroRequest;
import italo.sisbanco.model.request.conta.ContaSaveRequest;
import italo.sisbanco.model.response.conta.ContaResponse;
import italo.sisbanco.service.ContaService;
import italo.sisbanco.shared.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/conta")
public class ContaController {
	
	@Autowired
	private HttpUtil httpUtil;
	
	@Autowired
	private ContaService contaService;
	
	@PreAuthorize("hasAuthority('contaWRITE')")
	@PostMapping("/registra")
	public ResponseEntity<Object> registra( 
			@Valid @RequestBody ContaSaveRequest request, 
			HttpServletRequest httpRequest ) throws SistemaException {
			
		String accessToken = httpUtil.extractBearerToken( httpRequest );
		contaService.registra( request, accessToken ); 
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("hasAnyAuthority('contaWRITE', 'contaDonoWRITE')")
	@PutMapping("/altera/{contaId}")	
	public ResponseEntity<Object> altera(
			@PathVariable Long contaId, 
			@Valid @RequestBody ContaSaveRequest request ) throws SistemaException {
		contaService.altera( contaId, request );
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("hasAuthority('contaREAD')")
	@GetMapping("/get/{contaId}")
	public ResponseEntity<Object> get(
			@PathVariable Long contaId ) throws SistemaException {
		ContaResponse resp = contaService.get( contaId );
		return ResponseEntity.ok( resp );
	}
	
	@PreAuthorize("hasAuthority('contaREAD')")
	@GetMapping("/get/por-username/{username}")
	public ResponseEntity<Object> get(
			@PathVariable String username ) throws SistemaException {
		ContaResponse resp = contaService.getByUsername( username );
		return ResponseEntity.ok( resp );
	}
	
	@PreAuthorize("hasAuthority('contaREAD')")
	@GetMapping("/filtra")
	public ResponseEntity<Object> filtra(
			@Valid @RequestBody ContaFiltroRequest request ) throws SistemaException {
		List<ContaResponse> resp = contaService.filtra( request );
		return ResponseEntity.ok( resp );
	}
	
	@PreAuthorize("hasAnyAuthority('contaDELETE', 'contaDonoDELETE')")
	@DeleteMapping("/deleta/{contaId}")
	public ResponseEntity<Object> deleta(
			@PathVariable Long contaId ) throws SistemaException {
		contaService.deleta( contaId );
		return ResponseEntity.ok().build();
	}
	
}
