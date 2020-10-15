package br.com.buzzi.campeonatobrasileiro.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ClassificacaoDTO implements Comparable<ClassificacaoDTO> {

    @NotBlank
    private String time;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer posicao;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer pontos;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer jogos;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer vitorias;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer empates;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer derrotas;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer golsMarcados;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer golsSofridos;

    @Override
    public int compareTo(ClassificacaoDTO o) {
        return this.getPontos().compareTo(o.getPontos());
    }
}
