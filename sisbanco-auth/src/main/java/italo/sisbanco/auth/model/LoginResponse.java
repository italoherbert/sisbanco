package italo.sisbanco.auth.model;

import lombok.Data;

@Data
public class LoginResponse {

	private String accessToken;
	
	private String refreshToken;
	
}
