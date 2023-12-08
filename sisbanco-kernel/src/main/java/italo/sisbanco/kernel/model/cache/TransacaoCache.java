package italo.sisbanco.kernel.model.cache;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.redis.core.RedisHash;

import italo.sisbanco.kernel.enums.TransacaoTipo;
import lombok.Data;

@Data
@RedisHash("transacao")
public class TransacaoCache implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
		
	private long origContaId;
	
	private long destContaId;
	
	private double valor;
	
	private Date dataCriacao;
	
	private TransacaoTipo tipo;
	
	private OperacaoPendenteCache operacaoPendente;		
	
}
