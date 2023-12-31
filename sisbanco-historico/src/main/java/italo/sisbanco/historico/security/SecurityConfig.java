package italo.sisbanco.historico.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final String[] PUBLIC = {
		"/api/historico/v3/api-docs/**",
		"/api/historico/api-docs/**",
		"/api/historico/swagger-ui/**",

		"/api/historico/swagger-ui.html",
		"/api/historico/api-docs",		
	};
	
	@Autowired
	private AuthorizationFilter authorizationFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf( csrf -> csrf.disable() )
            .authorizeHttpRequests(authHttpReqs ->
                authHttpReqs
                    .requestMatchers(PUBLIC).permitAll()
                    .anyRequest().authenticated()
            )
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore( authorizationFilter, UsernamePasswordAuthenticationFilter.class );			
        
		return http.build();
	}

}
