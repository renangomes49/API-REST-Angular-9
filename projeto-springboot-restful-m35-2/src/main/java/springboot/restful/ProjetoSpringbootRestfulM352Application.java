package springboot.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EntityScan(basePackages = {"springboot.*"})
@ComponentScan(basePackages = {"springboot.*"}) //Para Injeção de Dependência
@EnableJpaRepositories(basePackages = {"springboot.restful.repository"})
@EnableTransactionManagement
@EnableWebMvc // Para a arquitetura MVC, porém nao será utilizado
@RestController // Para a arquitetura RestFul
@EnableAutoConfiguration
@EnableCaching
public class ProjetoSpringbootRestfulM352Application {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoSpringbootRestfulM352Application.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("admin"));
	}

	/*
	@Override // Cross Origin - Configuração Centralizada 
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**") // Liberando acesso a todos os controllers para qualquer um poder fazer uma requisição
		.allowedMethods("*")
		.allowedOrigins("*");
		
		// Liberando requições de POST e DELETE ao controller usuario para os clientes 1 e 2
		registry.addMapping("/usuario/**")
		.allowedMethods("POST","DELETE")
		.allowedOrigins("www.cliente1.com.br","www.cliente2.com.br");
	}
	*/ 
	
}
