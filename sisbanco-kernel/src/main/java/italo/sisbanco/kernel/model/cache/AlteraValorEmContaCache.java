package italo.sisbanco.kernel.model.cache;

import org.springframework.data.redis.core.RedisHash;

import italo.sisbanco.kernel.model.enums.ValorEmContaTipo;
import lombok.Data;

@Data
@RedisHash("altera_valor_em_conta")
public class AlteraValorEmContaCache {

	private String id;

	private long contaId;

	private double valor;
		
	private ValorEmContaTipo tipo;
	
	private OperacaoPendenteCache operacaoPendente;
	
}
