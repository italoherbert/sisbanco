package italo.sisbanco.kernel.apidoc.conta;

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

@Operation(
	summary = "Responsável por alterar dados de uma conta no sistema.",
	security = @SecurityRequirement(name = OpenAPIConfiguration.SECURITY_APP_NAME))	
@ApiResponses(value= {
	@ApiResponse( 		
		responseCode = "200",
		description = "Alteração de dados da conta efetuada com sucesso.",
		content = @Content),
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
public @interface AlteraContaEndpoint {
		
}
