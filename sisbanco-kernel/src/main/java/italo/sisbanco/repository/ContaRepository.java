package italo.sisbanco.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.sisbanco.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
	
	@Query("select c from Conta c where lower(c.titular) like lower(?1)")
	public List<Conta> filtra( String titular );
	
	@Query("select count(*)=1 from Conta where lower(titular)=lower(?1)")  
	public boolean existeTitular( String titular );
		
}
