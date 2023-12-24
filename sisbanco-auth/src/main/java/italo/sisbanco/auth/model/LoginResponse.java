package italo.sisbanco.auth.model;

import java.util.List;

import lombok.Data;

@Data
public class LoginResponse {
	
	private String accessToken;
	
	private String refreshToken;
	
	private String username;

	private List<String> roles;
	
}
