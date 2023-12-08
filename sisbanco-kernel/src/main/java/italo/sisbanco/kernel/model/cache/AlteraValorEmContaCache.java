package italo.sisbanco.kernel.model.cache;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.redis.core.RedisHash;

import italo.sisbanco.kernel.enums.AlteraValorEmContaTipo;
import lombok.Data;

@Data
@RedisHash("altera_valor_em_conta")
public class AlteraValorEmContaCache implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private long contaId;

	private double valor;
	
	private Date dataCriacao;
		
	private AlteraValorEmContaTipo tipo;
	
	private OperacaoPendenteCache operacaoPendente;
	
}
