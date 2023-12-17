package italo.sisbanco.kernel.security;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import italo.sisbanco.kernel.Erros;
import italo.sisbanco.kernel.exception.AuthorizatorException;
import italo.sisbanco.kernel.model.Conta;
import italo.sisbanco.kernel.repository.ContaRepository;

@Component
public class Authorizator {
		
	@Autowired
	private ContaRepository contaRepository;
		
	public boolean hasAuthority( String... authorities ) {
		Collection<? extends GrantedAuthority> authorities2 = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		Iterator<? extends GrantedAuthority> it2 = authorities2.iterator(); 
		while( it2.hasNext() ) {
			GrantedAuthority ga = it2.next();
			for( String a : authorities )
				if ( a.equalsIgnoreCase( ga.getAuthority() ) )			
					return true;
		}
		return false;
	}
	
	public void ownerAuthorize( Long contaId ) throws AuthorizatorException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Optional<Conta> contaOp = contaRepository.findById( contaId );
		if ( !contaOp.isPresent() )
			throw new AuthorizatorException( Erros.ACESSO_NAO_AUTORIZADO );
		
		Conta conta = contaOp.get();
		
		if ( !username.equalsIgnoreCase( conta.getUsername() ) )
			throw new AuthorizatorException( Erros.ACESSO_NAO_AUTORIZADO );			
	}
			
}
