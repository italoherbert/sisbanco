package italo.sisbanco.kernel.model.cache;

import org.springframework.data.redis.core.RedisHash;

import italo.sisbanco.kernel.model.enums.OperacaoPendenteTipo;
import lombok.Data;

@Data
@RedisHash("operacao_pendente")
public class OperacaoPendenteCache {

	private String id;
	
	private OperacaoPendenteTipo tipo;
	
}
