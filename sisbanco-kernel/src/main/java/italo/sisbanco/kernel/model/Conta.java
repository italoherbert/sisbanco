package italo.sisbanco.kernel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="conta")
public class Conta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String titular;
	
	private String username;
	
	private String userId;
	
	private double saldo;
	
	private double credito;
	
	private double limiteOperacao;
	
	private double limiteDiario;
	
}
