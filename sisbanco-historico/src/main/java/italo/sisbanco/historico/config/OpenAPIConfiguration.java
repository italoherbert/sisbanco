package italo.sisbanco.historico.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition( 
	servers = {
       @Server(url = "http://localhost:8080", description = "Servidor padrão")
    }
) 
@SecurityScheme(
		name = OpenAPIConfiguration.SECURITY_APP_NAME, 
		in = SecuritySchemeIn.HEADER, 
		type = SecuritySchemeType.HTTP, 
		scheme = "bearer", 
		bearerFormat = "JWT")
public class OpenAPIConfiguration {
	
	public final static String SECURITY_APP_NAME = "security-sisbanco-historico";
	
	public final static String ERRO_403_MSG = "Acesso não autorizado! Autentique-se com um usuário com as credenciais necessárias.";
	public final static String ERRO_400_MSG = "Erro de validação ou no servidor";
	
	/*
	@Bean
	OpenAPI customOpenAPI() {
		
		return new OpenAPI()	
				.components(new Components())
				.info(new Info().title("spring-cloud-function-webmvc OpenAPI Demo").version("1.0")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
	*/
}
