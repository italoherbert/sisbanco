package italo.sisbanco.kernel.repository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import italo.sisbanco.kernel.model.cache.TransacaoCache;

@Repository
public class OperTransacaoCacheRepository {
	
	public final static String KEY = "TRANSACAO";
	
	private HashOperations<String, String, TransacaoCache> hashOperations;
	
	public OperTransacaoCacheRepository(RedisTemplate<String, TransacaoCache> redisTemplate) {
		this.hashOperations = redisTemplate.opsForHash();		
	}
	
	@CachePut(value="transacao_cache", key="#p0") 
	public void save( TransacaoCache transacao ) {		
		hashOperations.put( KEY, transacao.getId(), transacao ); 		
	}
	
	@Cacheable(value="transacao_cache", key="#id")
	public Optional<TransacaoCache> findById( String id ) {
		TransacaoCache tc = hashOperations.get( KEY, id );		
		return Optional.ofNullable( tc );
	}
	
	public List<TransacaoCache> findByContaId( long contaId ) {
		return hashOperations.entries( KEY ).values().stream() 
				.filter( t -> t.getOrigContaId() == contaId )
				.toList();
	}
	
	public Optional<TransacaoCache> findByOperacaoPendenteId( String operacaoPendenteId ) {
		Iterator<TransacaoCache> transacoesCacheIT = hashOperations.entries( KEY ).values().iterator();
		
		TransacaoCache tcache = null;		
		while( transacoesCacheIT.hasNext() && tcache == null ) {
			TransacaoCache tcache2 = transacoesCacheIT.next();
			if ( tcache2.getOperacaoPendente().getId().equals( operacaoPendenteId ) ) {
				tcache = tcache2;
			}
		}
				
		System.out.println( "Não Achou= "+operacaoPendenteId );
		return Optional.ofNullable( tcache );		
	}
	
	public boolean existsById( String id ) {
		return hashOperations.hasKey( KEY, id );			
	}
	
	@CacheEvict(value="transacao_cache", key="#id") 
	public void deleteById( String id ) {
		hashOperations.delete( KEY, id );
	}	
	
	public void deleteAll() {
		hashOperations.entries( KEY ).values().stream().forEach( (tc) -> hashOperations.delete( KEY, tc.getId() ) );
	}
	
}
