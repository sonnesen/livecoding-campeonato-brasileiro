package br.com.buzzi.campeonatobrasileiro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeDTO {

    @NotBlank
    @Size(min = 3, max = 60)
    private String nome;

    @NotBlank
    @Size(min = 2, max = 2)
    private String estado;

    @NotBlank
    @Size(min = 3, max = 4)
    private String sigla;
}
