package italo.sisbanco.historico.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("transacao")
public class Transacao {

	@Id
	private String id;
	
	private String username;
		
	private double valor;
		
	private Date dataOperacao;
	
	private String tipo;
		
}
