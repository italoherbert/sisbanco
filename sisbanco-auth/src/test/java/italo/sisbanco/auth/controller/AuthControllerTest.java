package italo.sisbanco.auth.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import italo.sisbanco.auth.SisbancoAuthApplication;
import italo.sisbanco.auth.manager.KeycloakManager;
import italo.sisbanco.auth.model.Login;
import italo.sisbanco.auth.model.Token;
import italo.sisbanco.auth.model.UserSaveRequest;
import italo.sisbanco.auth.service.TokenService;
import italo.sisbanco.auth.service.UserService;
import jakarta.ws.rs.core.Response;

@SpringBootTest(classes = SisbancoAuthApplication.class)
public class AuthControllerTest {

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;	
						
	@MockBean
	private TokenService tokenService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private KeycloakManager keycloakManager;	
			
	@BeforeEach
	public void setUp() {		
		mockMvc = MockMvcBuilders
			.webAppContextSetup( context )
			.apply( springSecurity() ) 
			.build();		
		
		Keycloak keycloakAdmin = mock( Keycloak.class );
		Keycloak keycloakClient = mock( Keycloak.class );

		AccessTokenResponse accessTokenResp = mock( AccessTokenResponse.class );
		Response userResp = mock( Response.class );
		
		UserResource userResource = mock( UserResource.class );
		GroupRepresentation groupRepresentation = mock( GroupRepresentation.class );

		when( keycloakManager.getKeycloakAdmin() ).thenReturn( keycloakAdmin );
		when( keycloakManager.getKeycloakClient( anyString(), anyString() ) ).thenReturn( keycloakClient );
		when( keycloakManager.token( anyString(), anyString() ) ).thenReturn( accessTokenResp );		
		when( keycloakManager.criaUser( any( Keycloak.class ), anyString(), any( UserRepresentation.class ) ) ).thenReturn( userResp );
		when( keycloakManager.getUserResource( any( Keycloak.class), anyString(), anyString() ) ).thenReturn( userResource );
		when( keycloakManager.getGroupRepresentation( any( Keycloak.class), anyString(), anyString() ) ).thenReturn( groupRepresentation );
	}
	
	@Test
	public void loginTest() {
		Login login = new Login();
		login.setUsername( "_cliente_" );
		login.setPassword( "_cliente_" ); 
		
		try {
			mockMvc.perform(
				post( "/api/auth/login" )
					.contentType( MediaType.APPLICATION_JSON )
					.content( objectMapper.writeValueAsBytes( login ) ) )
				.andDo( print() )
					.andExpect( status().is( 403 ) );
		} catch (Exception e) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	public void tokenInfoTest() {
		Token token = new Token();
		token.setAccessToken( "abc" );
		try {
			mockMvc.perform(
				post( "/api/auth/token-info" )
					.contentType( MediaType.APPLICATION_JSON )
					.content( objectMapper.writeValueAsBytes( token ) ) )
				.andDo( print() )
					.andExpect( status().isOk() );
		} catch (Exception e) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@WithMockUser(username="user", authorities = {"userCreateWRITE"})
	public void registraUser() {
		UserSaveRequest user = new UserSaveRequest();
		user.setUsername( "user" );
		user.setPassword( "user" );
		user.setFirstName( "user" );
		user.setLastName( "user last" );
		user.setEmail( "user@outlook.com" );
		user.setGroupPath( "/user" ); 
				
		try {
			mockMvc.perform(
				post( "/api/auth/users" )
					.contentType( MediaType.APPLICATION_JSON )
					.content( objectMapper.writeValueAsBytes( user ) ) )
				.andDo( print() )
					.andExpect( status().isOk() );
		} catch (Exception e) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@WithMockUser(username="user", authorities = {"userDELETE"})
	public void deletaUser() {						
		try {
			mockMvc.perform(
					delete( "/api/auth/users/-1" ) )
				.andDo( print() )
					.andExpect( status().isOk() );
		} catch (Exception e) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
}
