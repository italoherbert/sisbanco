package italo.sisbanco.trans.reg;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableRabbit
@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class SisbancoTransRegApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisbancoTransRegApplication.class, args);
	}

}
