package italo.sisbanco.trans.reg.integration.model;

import java.util.List;

import lombok.Data;

@Data
public class TokenInfo {

	private String username;
	
	private List<String> roles;
	
}
