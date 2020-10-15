package br.com.buzzi.campeonatobrasileiro.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(name = "gols_time_mandante", columnDefinition = "integer default 0")
    private Integer golsTimeMandante;

    @Column(name = "gols_time_visitante", columnDefinition = "integer default 0")
    private Integer golsTimeVisitante;

    @Column(name = "publico_pagante", columnDefinition = "integer default 0")
    private Integer publicoPagante;

    @Column(columnDefinition = "boolean default false")
    private Boolean encerrado;

    @Column(columnDefinition = "integer default 0")
    private Integer rodada;

    @ManyToOne
    @JoinColumn(name = "id_time_mandante")
    private Time timeMandante;

    @ManyToOne
    @JoinColumn(name = "id_time_visitante")
    private Time timeVisitante;
}
