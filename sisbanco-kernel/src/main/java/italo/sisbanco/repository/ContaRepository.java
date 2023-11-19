package italo.sisbanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import italo.sisbanco.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
	
	@Modifying
	@Query("update conta set saldo=?2 where id=?1" )
	public void alteraSaldo( Long contaId, double novoSaldo );
	
}
