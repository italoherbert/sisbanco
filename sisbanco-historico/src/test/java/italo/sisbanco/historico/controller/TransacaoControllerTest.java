package italo.sisbanco.historico.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import italo.sisbanco.historico.SisbancoHistoricoApplication;
import italo.sisbanco.historico.ext.rabbitmq.RabbitTestConfiguration;
import italo.sisbanco.historico.message.TransacaoMesseger;
import italo.sisbanco.historico.service.TransacaoService;

@SpringBootTest(classes=SisbancoHistoricoApplication.class)
@Import(RabbitTestConfiguration.class)
public class TransacaoControllerTest {
			
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@MockBean
	private TransacaoService transacaoService;
		
	@MockBean
	private TransacaoMesseger transacaoMessager;
					
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
					get("/api/historico/transacoes/ultimas/10/lista" ) )
				.andDo( print() )
				.andExpect( status().isOk() );
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}
	
	@Test
	@WithMockUser(username="test", authorities = {"historicoTransacoesREAD"})
	public void testFiltra() {				
		try {			
			mockMvc.perform( 
					get("/api/historico/transacoes?username=test&dataInicio=01/01/2023&dataFim=01/01/2023" ) )
				.andDo( print() )
				.andExpect( status().isOk() );				
		} catch ( Exception e ) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}

}
