package italo.sisbanco.kernel.model.cache;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import italo.sisbanco.kernel.enums.OperacaoPendenteTipo;
import lombok.Data;

@Data
@RedisHash("operacao_pendente")
public class OperacaoPendenteCache implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private OperacaoPendenteTipo tipo;
	
}
