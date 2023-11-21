package italo.sisbanco.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/conta")
public class ContaController {

	@Autowired
	private ContaService contaService;
	
	@PostMapping("/registra")
	public ResponseEntity<Object> registra( 
			@Valid @RequestBody ContaSaveRequest request ) throws SistemaException {		
		contaService.registra( request );
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/altera/{contaId}")	
	public ResponseEntity<Object> altera(
			@PathVariable Long contaId, 
			@Valid @RequestBody ContaSaveRequest request ) throws SistemaException {
		contaService.altera( contaId, request );
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/get/{contaId}")
	public ResponseEntity<Object> get(
			@PathVariable Long contaId ) throws SistemaException {
		ContaResponse resp = contaService.get( contaId );
		return ResponseEntity.ok( resp );
	}
	
	@GetMapping("/filtra")
	public ResponseEntity<Object> filtra(
			@Valid @RequestBody ContaFiltroRequest request ) throws SistemaException {
		List<ContaResponse> resp = contaService.filtra( request );
		return ResponseEntity.ok( resp );
	}
	
	@DeleteMapping("/deleta/{contaId}")
	public ResponseEntity<Object> deleta(
			@PathVariable Long contaId ) throws SistemaException {
		contaService.deleta( contaId );
		return ResponseEntity.ok().build();
	}
	
}
