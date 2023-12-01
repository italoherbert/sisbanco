package italo.sisbanco.kernel.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.integration.KeycloakMicroserviceIntegration;
import italo.sisbanco.kernel.integration.model.Token;
import italo.sisbanco.kernel.integration.model.TokenInfo;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.repository.ContaRepository;
import italo.sisbanco.shared.util.HttpUtil;

@Component
public class Authorizator {
		
	@Autowired
	private HttpUtil httpUtil;

	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private KeycloakMicroserviceIntegration keycloak;
	
	public void ownerAuthorize( String authorizationHeader, Long contaId ) throws AuthorizatorException {						
		String accessToken = httpUtil.extractBearerToken( authorizationHeader );
		if ( accessToken == null )
			throw new AuthorizatorException( Erros.ACESSO_NAO_AUTORIZADO );
		
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new AuthorizatorException( Erros.ACESSO_NAO_AUTORIZADO );
		
		Conta conta = contaOp.get();
		
		Token token = new Token();
		token.setAccessToken( accessToken );
		
		ResponseEntity<TokenInfo> resp = keycloak.tokenInfo( token );
		if ( !resp.getStatusCode().is2xxSuccessful() )
			throw new AuthorizatorException( Erros.ACESSO_NAO_AUTORIZADO );
		
		if ( !resp.getBody().getUsername().equalsIgnoreCase( conta.getUsername() ) )
			throw new AuthorizatorException( Erros.ACESSO_NAO_AUTORIZADO );		
	}
	
}