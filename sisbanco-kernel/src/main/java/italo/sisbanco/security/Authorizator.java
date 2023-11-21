package italo.sisbanco.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.sisbanco.util.TokenUtil;

@Component
public class Authorizator {
	
	@Autowired
	private TokenUtil tokenUtil;

	public boolean ownerAuthorize( String authorizationHeader, Long userId ) {
		
	}
	
}
