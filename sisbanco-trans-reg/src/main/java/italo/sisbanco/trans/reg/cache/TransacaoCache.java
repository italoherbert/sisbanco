package italo.sisbanco.trans.reg.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import italo.sisbanco.trans.reg.model.CacheTransacao;

@Component
public class TransacaoCache {

	public final static String KEY = "TRANSACAO";
	
	public RedisTemplate<String, CacheTransacao> redisTemplate;
	public HashOperations<String, Long, CacheTransacao> hashOperations;
	
	public TransacaoCache( RedisTemplate<String, CacheTransacao> redisTemplate ) {
		this.redisTemplate = redisTemplate;
		this.hashOperations = redisTemplate.opsForHash();
	}
	
	@CachePut(value="transacao_cache", key="#p0") 
	public void setTransacao( CacheTransacao transacao ) {
		hashOperations.put( KEY, transacao.getId(), transacao ); 
	}
	
	@Cacheable(value="transacao_cache", key="#id")
	public CacheTransacao getTransacao( Long id ) {
		return hashOperations.get( KEY, id );
	}
	
	@CacheEvict(value="transacao_cache", key="#id") 
	public void deletaTransacao( Long id ) {
		hashOperations.delete( KEY, id );
	}
	
}
