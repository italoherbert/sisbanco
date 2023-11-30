package italo.sisbanco.kernel.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import italo.sisbanco.kernel.model.cache.TransacaoCache;

@Repository
public class TransacaoCacheRepository {
	
	public final static String KEY = "TRANSACAO";
	
	private HashOperations<String, String, TransacaoCache> hashOperations;
	
	public TransacaoCacheRepository(RedisTemplate<String, TransacaoCache> redisTemplate) {
		this.hashOperations = redisTemplate.opsForHash();		
	}
	
	@CachePut(value="transacao_cache", key="#p0") 
	public void save( TransacaoCache transacao ) {
		String id = transacao.getId();
		if ( id == null ) {
			id =  UUID.randomUUID().toString();
			transacao.setId( id );
		}
		hashOperations.put( KEY, id, transacao ); 
	}
	
	@Cacheable(value="transacao_cache", key="#contaId")
	public List<TransacaoCache> findByContaId( Long contaId ) {
		return hashOperations.entries( KEY ).values().stream()
				.filter( t -> t.getOrigContaId() == contaId )
				.toList();
	}
	
	@Cacheable(value="transacao_cache", key="#id")
	public Optional<TransacaoCache> findById( String id ) {
		TransacaoCache tcache = hashOperations.get( KEY, id );
		return Optional.of( tcache );
	}
	
	@Cacheable(value="transacao_cache", key="#id")
	public boolean existsById( String id ) {
		Boolean exists = hashOperations.hasKey( KEY, id );
		if ( exists == null )
			return false;
		return exists.booleanValue();
	}
	
	@CacheEvict(value="transacao_cache", key="#id") 
	public void deleteById( String id ) {
		hashOperations.delete( KEY, id );
	}	
	
}
