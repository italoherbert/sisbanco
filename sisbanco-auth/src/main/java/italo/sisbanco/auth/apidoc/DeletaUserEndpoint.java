package italo.sisbanco.auth.apidoc;

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
import italo.sisbanco.auth.config.OpenAPIConfiguration;
import italo.sisbanco.auth.model.ErroResponse;
import italo.sisbanco.auth.model.UserCreated;

@Operation(
	summary = "Responsável pela remoção de usuário no keycloak",
	security = @SecurityRequirement(name = OpenAPIConfiguration.SECURITY_APP_NAME))	
@ApiResponses(value= {
	@ApiResponse( 		
		responseCode = "200",
		description = "Remoção bem sucedida.",
		content = {@Content(					
			mediaType = "application/json", 
			schema = @Schema(implementation = UserCreated.class))}),
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
public @interface DeletaUserEndpoint {
		
}
