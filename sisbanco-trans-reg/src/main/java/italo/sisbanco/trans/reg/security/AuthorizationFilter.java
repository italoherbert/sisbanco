package italo.sisbanco.trans.reg.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import feign.FeignException.FeignClientException;
import italo.sisbanco.shared.util.HttpUtil;
import italo.sisbanco.trans.reg.integration.KeycloakMicroserviceIntegration;
import italo.sisbanco.trans.reg.integration.model.Token;
import italo.sisbanco.trans.reg.integration.model.TokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
	
	@Autowired
	private KeycloakMicroserviceIntegration keycloak;
		
	@Autowired
	private HttpUtil httpUtil; 
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String accessToken = httpUtil.extractBearerToken( request );
		if ( accessToken == null ) {
			super.doFilter( request, response, filterChain ); 
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
			httpUtil.sendErrorResponse( response, e.contentUTF8() );
			return;
		}

		super.doFilter( request, response, filterChain ); 
	}
	
}
