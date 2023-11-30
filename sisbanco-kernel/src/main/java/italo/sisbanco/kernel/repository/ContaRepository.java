package italo.sisbanco.kernel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.sisbanco.kernel.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
	
	@Query("select c "
			+ "from Conta c "
			+ "where (?1 = '*%') or ( lower(c.titular) like lower(?1) )")
	public List<Conta> filtra( String titular );
	
	@Query("select c.username from Conta c where c.id=?1")
	public String buscaUsername( Long contaId );
	
	@Query("select c from Conta c where c.username=?1")
	public Optional<Conta> buscaPorUsername( String username );
	
	@Query("select count(*)=1 from Conta where lower(titular)=lower(?1)")  
	public boolean existeTitular( String titular );
		
}
