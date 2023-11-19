package italo.sisbanco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.sisbanco.model.request.CreditoRequest;
import italo.sisbanco.model.response.CreditoResponse;

@RestController
@RequestMapping("/api/banco")
public class BancoController {
	
	@PostMapping("/credita/{contaId}")
	public ResponseEntity<Object> credita( 
			@PathVariable Long contaId, 
			@RequestBody CreditoRequest request ) {
		
		CreditoResponse resp = new CreditoResponse();
		resp.setSaldo( request.getValor() );
		return ResponseEntity.ok( resp );		
	}
	
}
