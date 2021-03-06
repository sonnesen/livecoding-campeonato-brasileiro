package br.com.buzzi.campeonatobrasileiro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JogoDTO {

    @NotNull
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime data;

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

    @NotNull
    private Boolean encerrado;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer rodada;

    @NotNull
    private TimeDTO timeMandante;

    @NotNull
    private TimeDTO timeVisitante;
}
