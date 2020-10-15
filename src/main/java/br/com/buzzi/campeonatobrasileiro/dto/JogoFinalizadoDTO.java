package br.com.buzzi.campeonatobrasileiro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JogoFinalizadoDTO {

    @NotNull
    @Min(value = 0)
    @Max(value = 99)
    private Integer golsTimeMandante;

    @NotNull
    @Min(value = 0)
    @Max(value = 99)
    private Integer golsTimeVisitante;

    @NotNull
    @Min(value = 0)
    @Max(value = Integer.MAX_VALUE)
    private Integer publicoPagante;
}
