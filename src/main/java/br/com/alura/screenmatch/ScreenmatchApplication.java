package br.com.alura.screenmatch;

import br.com.alura.screenmatch.principal.Principal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}
// NO VA ESTO
		//var consumoApi = new ConsumoApi(); //ConsumoApi al principio da lo mismo
		//var json = consumoApi.obterDados("https://www.omdbapi.com/?t=yellowstone&apikey=91b1b6f7");
		//System.out.println(json);



