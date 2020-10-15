package br.com.buzzi.campeonatobrasileiro.repository;

import br.com.buzzi.campeonatobrasileiro.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {

    List<Time> findByNomeIgnoreCaseAndIdNot(String nome, Long id);
}