package italo.sisbanco.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenUtil {

	public String extractToken( HttpServletRequest request ) {
		String authorizationHeader = request.getHeader( "Authorization" );
		return this.extractToken( authorizationHeader );
	}
	
	public String extractToken( String authhorizationHeader ) {
		if ( authhorizationHeader != null )
			if ( authhorizationHeader.startsWith( "Bearer " ) ) 
				return authhorizationHeader.substring( 7 );				
		return null;
	}
	
}
