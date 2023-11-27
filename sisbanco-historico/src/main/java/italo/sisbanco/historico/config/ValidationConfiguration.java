package italo.sisbanco.historico.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfiguration {

	@Bean
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = 
				new ReloadableResourceBundleMessageSource();
		
		messageSource.setBasename( "classpath:messages" );
		messageSource.setDefaultEncoding( "UTF-8" );
		return messageSource;
	}
	
	@Bean
	LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();		
		validatorFactory.setValidationMessageSource( messageSource() );
		return validatorFactory;
				
	}
	
}
