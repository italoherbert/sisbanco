package italo.sisbanco;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import italo.sisbanco.kernel.SisbancoKernelApplication;

@SpringBootTest(classes=SisbancoKernelApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class SisbancoKernelApplicationTests {
		
	//private JdbcTemplate jdbcTemplate;

	/*
	@BeforeEach
	public void setUp() {
		DataSource datasource = new EmbeddedDatabaseBuilder()
				.setType( EmbeddedDatabaseType.H2 )
				.addScript( "/data/data.sql")
				.build();
		jdbcTemplate = new JdbcTemplate( datasource );
		jdbcTemplate.update( "insert into conta ( titular, username, saldo, credito ) values ( 'joao', 'joao', 0, 0 )" );
		jdbcTemplate.update( "insert into conta ( titular, username, saldo, credito ) values ( 'maria', 'maria', 0, 0 )" );
	}
	
	@AfterEach
	public void tearDown() {
		jdbcTemplate.execute( "drop table conta" );
	}
			
	public int buscaContaId( String username ) throws Exception {
		return jdbcTemplate.queryForObject( "select id from conta where username='"+username+"'", Integer.class );
	}	
	*/
	
	@Test
	void contextLoads() {
	}

}
