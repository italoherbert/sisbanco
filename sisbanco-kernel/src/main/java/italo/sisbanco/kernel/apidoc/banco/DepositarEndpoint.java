package italo.sisbanco.kernel.apidoc.banco;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import italo.sisbanco.kernel.config.OpenAPIConfiguration;
import italo.sisbanco.kernel.model.response.ErroResponse;
import italo.sisbanco.kernel.model.response.conta.TransacaoResponse;

@Operation(
	summary = "Responsável por efetuar o depósito em conta pelo titular.",
	security = @SecurityRequirement(name = OpenAPIConfiguration.SECURITY_APP_NAME))	
@ApiResponses(value= {
	@ApiResponse( 		
		responseCode = "200",
		description = "Depósito realizado com sucesso.",
		content = {@Content(					
			mediaType = "application/json", 
			schema = @Schema(implementation = TransacaoResponse.class))}),
	@ApiResponse(
		responseCode = "403",
		description = OpenAPIConfiguration.ERRO_403_MSG,
			content=@Content(					
				mediaType = "application/json", 
				schema = @Schema(implementation = ErroResponse.class))),
	@ApiResponse(
		responseCode = "400",
		description = OpenAPIConfiguration.ERRO_400_MSG,
		content=@Content(					
			mediaType = "application/json", 
			schema = @Schema(implementation = ErroResponse.class)))
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DepositarEndpoint {
		
}
