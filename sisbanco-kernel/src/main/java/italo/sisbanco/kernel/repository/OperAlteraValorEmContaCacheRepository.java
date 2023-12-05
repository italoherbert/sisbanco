package italo.sisbanco.kernel.repository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import italo.sisbanco.kernel.model.cache.AlteraValorEmContaCache;

@Repository
public class OperAlteraValorEmContaCacheRepository {

	public final static String KEY = "valor";
	
	private HashOperations<String, String, AlteraValorEmContaCache> hashOperations;
	
	public OperAlteraValorEmContaCacheRepository(RedisTemplate<String, AlteraValorEmContaCache> redisTemplate) {
		this.hashOperations = redisTemplate.opsForHash();		
	}
	
	@CachePut(value="valor_em_conta_cache", key="#p0") 
	public void save( AlteraValorEmContaCache valor ) {
		String id = valor.getId();
		if ( id == null ) {
			id =  UUID.randomUUID().toString();
			valor.setId( id );
		}
		hashOperations.put( KEY, id, valor ); 
	}
	
	@Cacheable(value="transacao_cache", key="#id")
	public Optional<AlteraValorEmContaCache> findById( String id ) {
		AlteraValorEmContaCache alterVC = hashOperations.get( KEY, id );		
		return Optional.ofNullable( alterVC );
	}
	
	public List<AlteraValorEmContaCache> findByContaId( Long contaId ) {
		return hashOperations.entries( KEY ).values().stream() 
				.filter( t -> t.getContaId() == contaId )
				.toList();
	}
			
	public Optional<AlteraValorEmContaCache> findByOperacaoPendenteId( String operacaoPendenteId ) {
		Iterator<AlteraValorEmContaCache> alterValorCacheIT = hashOperations.entries( KEY )
				.values().iterator();
				
		AlteraValorEmContaCache alterValorCache = null;
		while( alterValorCacheIT.hasNext() && alterValorCache == null ) {
			AlteraValorEmContaCache alterValorCache2 = alterValorCacheIT.next();
			if ( alterValorCache2.getOperacaoPendente().getId() == operacaoPendenteId ) 
				alterValorCache = alterValorCache2;			
		}
					
		return Optional.ofNullable( alterValorCache );
	}
	
	public boolean existsById( String id ) {
		return hashOperations.hasKey( KEY, id );			
	}
	
	@CacheEvict(value="valor_em_conta_cache", key="#id") 
	public void deleteById( String id ) {
		hashOperations.delete( KEY, id );
	}	
	
	
}
