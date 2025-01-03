package br.com.alura.screenmach;

import br.com.alura.screenmach.principal.Principal;
import br.com.alura.screenmach.service.ConsumoApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmachApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(ScreenmachApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();


		var consumoApi = new ConsumoApi(); //ConsumoApi al principio da lo mismo
		//var json = consumoApi.obterDados("https://www.omdbapi.com/?t=yellowstone&apikey=91b1b6f7");
		//System.out.println(json);


	}
}
