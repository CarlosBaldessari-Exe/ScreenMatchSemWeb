package br.com.alura.screenmatch.principal;


import br.com.alura.screenmatch.model.DadosEpisodios;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import javax.swing.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.Collectors;

public class Principal {

    private final String ENDERECO="https://www.omdbapi.com/?t=";

    private final String API_KEY="&apikey=91b1b6f7";

    private  ConsumoApi consumo = new ConsumoApi();
    Scanner sc = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu(){


        System.out.printf("Digite o nome da serie para busca ");
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

//        for(int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j< episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

//        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
//
//        nomes.stream().sorted().limit(3).filter(n -> n.startsWith("N")).map(n -> n.toUpperCase())
//                .forEach(System.out::println);

        List<DadosEpisodios> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

//        System.out.println("\nTop 5 episódios");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e-> System.out.println("Primero Filtro (N/A)"))
//                .sorted(Comparator.comparing(DadosEpisodios ::avaliacao).reversed())
//                .peek(e-> System.out.println(" Ordenacao "))
//                .limit(5)
//                .peek(e-> System.out.println("Limite"))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e-> System.out.println("Mapeo"))
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.printf("Digite una parte del titulo del episodio ");
        var trechoTitulo = sc.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if (episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado com sucesso! ");
            System.out.println("println();Temporada: " + episodioBuscado.get().getTemporada() + " O nome do episodio: " + episodioBuscado.get().getTitulo());
        }else {
            System.out.println("Episodio nao encontrado! ");
        }

//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        var ano = sc.nextInt();
//        sc.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada:  " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data lançamento: " + e.getDataLancamento().format(formatador)
//                ));

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao()> 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada.toString());

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao()> 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Media " + est.getAverage());
        System.out.println("Melhor episodio: " + est.getMax());
        System.out.println("Pior episodio: "+ est.getMin());
        System.out.println("Quantidade: " + est.getCount());


    }
}