package italo.sisbanco.historico.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import italo.sisbanco.historico.model.Transacao;

public interface TransacaoRepository extends MongoRepository<Transacao, String> {

	@Query(value="{}", sort="{dataOperacao:-1}") 
	public List<Transacao> listaOrderDesc( Pageable p );
	
	@Query(value="{username: ?0, dataOperacao: { $gte: ?1, $lte: $2 }}")
	public List<Transacao> filtra( 
			String username, Date dataIni, Date dataFim );
	
}
