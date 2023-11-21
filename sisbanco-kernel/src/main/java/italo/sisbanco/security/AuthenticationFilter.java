package italo.sisbanco.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import feign.FeignException.FeignClientException;
import italo.sisbanco.integration.KeycloakMicroserviceIntegration;
import italo.sisbanco.integration.model.Token;
import italo.sisbanco.integration.model.TokenInfo;
import italo.sisbanco.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private KeycloakMicroserviceIntegration keycloak;
		
	@Autowired
	private TokenUtil tokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String accessToken = tokenUtil.extractToken( request );
				
		if ( accessToken == null ) {
			String resp = "{ \"mensagem\" : \"Token inválido.\" }";
			response.setContentType( "application/json" );
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
			
			PrintWriter writer = new PrintWriter( response.getOutputStream() );
			writer.print( resp ); 
			writer.flush();		
			
			return;
		}
			
		Token token = new Token();
		token.setAccessToken( accessToken );
		
		try {
			ResponseEntity<TokenInfo> resp = keycloak.tokenInfo( token );
			if ( resp.getStatusCode().is2xxSuccessful() ) {
				TokenInfo tokenInfo = resp.getBody();
				
				String username = tokenInfo.getUsername();
				List<String> roles = tokenInfo.getRoles();
				
				List<SimpleGrantedAuthority> authorities = 
						roles.stream().map( r -> new SimpleGrantedAuthority( r ) ).toList();
										
				UsernamePasswordAuthenticationToken userPassToken = 
						new UsernamePasswordAuthenticationToken( username, null, authorities );
				
				SecurityContextHolder.getContext().setAuthentication( userPassToken );
			} else {
				System.out.println( "ERRO:  "+resp.getStatusCode().value() );
			}
		} catch ( FeignClientException e ) {			
			response.setContentType( "application/json" );
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
			
			PrintWriter writer = new PrintWriter( response.getOutputStream() );
			writer.print( e.contentUTF8() ); 
			writer.flush();
			return;
		}							

		super.doFilter( request, response, filterChain );
	}
	
}
