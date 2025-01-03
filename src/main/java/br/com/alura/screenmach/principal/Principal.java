package br.com.alura.screenmach.principal;

import br.com.alura.screenmach.model.DadosEpisodios;
import br.com.alura.screenmach.model.DadosSerie;
import br.com.alura.screenmach.model.DadosTemporada;
import br.com.alura.screenmach.model.Episodio;
import br.com.alura.screenmach.service.ConsumoApi;
import br.com.alura.screenmach.service.ConverteDados;

import javax.swing.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final String ENDERECO="https://www.omdbapi.com/?t=";

    private final String API_KEY="&apikey=91b1b6f7";

    private  ConsumoApi consumo = new ConsumoApi();
    Scanner sc = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu(){


        System.out.printf("Digite o nome da serie para busca");
        String nomeSerie = sc.nextLine();

        var json= consumo.obterDados(  ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados= conversor.obterDados(json, DadosSerie.class);
        System.out.printf(dados.toString());


        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i<=dados.totalTemporadas(); i++){
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);


        }
        temporadas.forEach(System.out::println);

//
//        for( int i = 0; i <dados.totalTemporadas(); i++ ){
//            List <DadosEpisodios> episodiosTemporada =temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.printf(episodiosTemporada.get(j).titulo());
//            }
//        }
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        temporadas.forEach(System.out::println);


        List<DadosEpisodios> dadosEpisodios = temporadas.stream()
                .flatMap(t->t.episodios().stream())
                .toList();

        System.out.printf("Top 5 Episodios");

        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                .limit(5);
        dadosEpisodios.forEach(System.out::println);

        List<Episodio> episodios =  temporadas.stream()
                .flatMap(t->t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))).toList();

        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano voce deceja ver os episodios");
        var ano = sc.nextInt();
        sc.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getAnoLancamento() != null && e.getAnoLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada" + e.getTemporada() +
                                "Episodio" + e.getTitulo() +
                                "Data de Lancamento" + e.getAnoLancamento().format(formato)

                ));
    }
}

