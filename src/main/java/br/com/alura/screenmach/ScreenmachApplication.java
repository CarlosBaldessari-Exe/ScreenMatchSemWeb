package br.com.alura.screenmach;

import br.com.alura.screenmach.model.DadosEpisodios;
import br.com.alura.screenmach.model.DadosSerie;
import br.com.alura.screenmach.service.ConsumoApi;
import br.com.alura.screenmach.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.temporal.ValueRange;

@SpringBootApplication
public class ScreenmachApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(ScreenmachApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var consumoApi = new ConsumoApi(); //ConsumoApi al principio da lo mismo
		var json= consumoApi.obterDados("https://www.omdbapi.com/?t=yellowstone&apikey=6585022c");
		System.out.println(json);

		ConverteDados conversor = new ConverteDados();
		DadosSerie dados= conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
		json = consumoApi.obterDados("https://www.omdbapi.com/?t=yellowstone&season=1&episode=5&apikey=6585022c");
		DadosEpisodios dadosEpisodios = conversor.obterDados(json, DadosEpisodios.class);
		System.out.println(dadosEpisodios);

	}
}
