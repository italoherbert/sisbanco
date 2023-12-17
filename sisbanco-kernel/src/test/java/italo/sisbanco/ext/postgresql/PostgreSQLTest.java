package italo.sisbanco.ext.postgresql;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class PostgreSQLTest {

	private static PostgreSQLContainer<?> postgreSQLContainer;		
	
	static {
		postgreSQLContainer = new PostgreSQLContainer<>( DockerImageName.parse( "postgres:15-alpine" ) )
				.withUsername( "postgres" )
				.withPassword( "postgres" )
				.withDatabaseName( "test" );
		postgreSQLContainer.start();		
	}
	
	@DynamicPropertySource
	public static void registerRedisProperties(DynamicPropertyRegistry registry) {
	    registry.add( "spring.datasource.url", postgreSQLContainer::getJdbcUrl );
	    registry.add( "spring.datasource.username", postgreSQLContainer::getUsername );
	    registry.add( "spring.datasource.password", postgreSQLContainer::getPassword );
	}	
	
}

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
