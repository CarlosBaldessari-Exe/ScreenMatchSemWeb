package br.com.alura.screenmach.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodios(@JsonAlias("Title") String Titulo,
                             @JsonAlias("Episode") Integer numero,
                             @JsonAlias("imdbRating") String avaliacao,
                             @JsonAlias("Released") String anoLancamento) {

}
