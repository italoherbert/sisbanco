package italo.sisbanco.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SisbancoGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisbancoGatewayApplication.class, args);
	}

}
