package italo.sisbanco.auth.security;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import italo.sisbanco.auth.exception.ErrorException;
import italo.sisbanco.auth.model.Token;
import italo.sisbanco.auth.model.TokenInfo;
import italo.sisbanco.auth.service.TokenService;
import italo.sisbanco.shared.util.HttpUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private MessageSource messageSource;	
		
	private final HttpUtil httpUtil = new HttpUtil(); 
	
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
			TokenInfo tokenInfo = tokenService.tokenInfo( token ); 
			
			String username = tokenInfo.getUsername();
			List<String> roles = tokenInfo.getRoles();
			
			List<SimpleGrantedAuthority> authorities = 
					roles.stream().map( r -> new SimpleGrantedAuthority( r ) ).toList();
									
			UsernamePasswordAuthenticationToken userPassToken = 
					new UsernamePasswordAuthenticationToken( username, null, authorities );
			
			SecurityContextHolder.getContext().setAuthentication( userPassToken );		
		} catch ( ErrorException e ) {
			String message = messageSource.getMessage( e.getErrorChave(), e.getErrorParams(), Locale.getDefault() );
			httpUtil.sendErrorResponse( response, 
					HttpServletResponse.SC_UNAUTHORIZED,
					"{ \"mensagem\": \""+message+"\"}" );
			return;
		}

		super.doFilter( request, response, filterChain ); 
	}
	
}
