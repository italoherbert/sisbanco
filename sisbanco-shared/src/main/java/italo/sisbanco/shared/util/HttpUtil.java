package italo.sisbanco.shared.util;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpUtil {

	public String extractBearerToken( HttpServletRequest request ) {
		String authorizationHeader = request.getHeader( "Authorization" );
		return this.extractBearerToken( authorizationHeader );
	}
	
	public String extractBearerToken( String authorizationHeader ) {
		if ( authorizationHeader != null )
			if ( authorizationHeader.startsWith( "Bearer " ) ) 
				return authorizationHeader.substring( 7 );				
		return null;
	}
		
	public void sendErrorResponse( HttpServletResponse response, String content ) throws ServletException, IOException {
		response.setContentType( "application/json" );
		response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		
		PrintWriter writer = new PrintWriter( response.getOutputStream() );
		writer.print( content ); 
		writer.flush();		
	}
	
	
	
}
