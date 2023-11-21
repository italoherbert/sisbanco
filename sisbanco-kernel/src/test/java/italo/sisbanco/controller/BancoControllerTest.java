package italo.sisbanco.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import italo.sisbanco.model.request.conta.ValorRequest;
import italo.sisbanco.service.BancoService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(BancoController.class)
public class BancoControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BancoService bancoService;
	
	private final ObjectMapper objMapper = new ObjectMapper();
	
	@Test
	public void testCredito() {
		ValorRequest req = new ValorRequest();
		req.setValor( 100 );
		
		try {
			mockMvc.perform( 
				MockMvcRequestBuilders.post( "/api/banco/creditar/1" )
					.contentType( MediaType.APPLICATION_JSON )
					.content( objMapper.writeValueAsBytes( req ) )
			).andDo(print())
				.andExpect( status().isOk() );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
