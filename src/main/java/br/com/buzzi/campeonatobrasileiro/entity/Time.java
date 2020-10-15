package br.com.buzzi.campeonatobrasileiro.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 60, nullable = false)
    private String nome;

    @Column(length = 2, nullable = false)
    private String estado;

    @Column(length = 4, nullable = false)
    private String sigla;
}
