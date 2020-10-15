package br.com.buzzi.campeonatobrasileiro.repository;

import br.com.buzzi.campeonatobrasileiro.entity.Jogo;
import br.com.buzzi.campeonatobrasileiro.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {

    List<Jogo> findByTimeMandanteAndEncerrado(Time timeMandante, Boolean encerrado);

    List<Jogo> findByTimeVisitanteAndEncerrado(Time timeVisitante, Boolean encerrado);

}