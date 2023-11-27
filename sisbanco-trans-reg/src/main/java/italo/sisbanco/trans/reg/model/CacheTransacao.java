package italo.sisbanco.trans.reg.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class CacheTransacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String origContaUsername;
	
	private String destContaUsername;
	
	private double valor;
	
	private Date dataOperacao;
	
	private String tipo;
	
}
