package italo.sisbanco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SisbancoKernelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisbancoKernelApplication.class, args);
	}

}
