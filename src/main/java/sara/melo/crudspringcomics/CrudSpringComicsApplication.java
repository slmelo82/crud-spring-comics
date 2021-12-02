package sara.melo.crudspringcomics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CrudSpringComicsApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrudSpringComicsApplication.class, args);
	}
}
