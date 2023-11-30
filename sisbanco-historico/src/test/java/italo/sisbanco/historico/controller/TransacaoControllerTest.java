package italo.sisbanco.historico.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import italo.sisbanco.historico.model.request.TransacaoFiltroRequest;
import italo.sisbanco.historico.service.TransacaoService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransacaoControllerTest {
		
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@MockBean
	private TransacaoService transacaoService;
									
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup( context )
				.apply( springSecurity())
				.build();
	
	}		
	
	@Test
	@WithMockUser(username="test", authorities = {"historicoTransacoesREAD"})
	public void testLista() {				
		try {						
			mockMvc.perform( 
					get("/api/historico/transacoes/lista/ultimas/10" ) )
				.andDo( print() )
				.andExpect( status().isOk() );
		} catch ( Exception e ) {
			
		}
	}
	
	@Test
	@WithMockUser(username="test", authorities = {"historicoTransacoesREAD"})
	public void testFiltra() {				
		try {
			TransacaoFiltroRequest request = new TransacaoFiltroRequest();
			request.setUsername( "test" );
			request.setDataInicio( new Date() );
			request.setDataFim( new Date() );
			
			mockMvc.perform( 
					post("/api/historico/transacoes/filtra" )
						.contentType( MediaType.APPLICATION_JSON )
						.content( objectMapper.writeValueAsBytes( request ) ) )
				.andDo( print() )
				.andExpect( status().isOk() );				
		} catch ( Exception e ) {
			
		}
	}

}
