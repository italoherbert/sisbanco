package italo.sisbanco.kernel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableCaching
@EnableRedisRepositories
@EnableFeignClients
@SpringBootApplication
public class SisbancoKernelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisbancoKernelApplication.class, args);
	}

}
