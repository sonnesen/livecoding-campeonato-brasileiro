package br.com.buzzi.campeonatobrasileiro.resource;

import br.com.buzzi.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.buzzi.campeonatobrasileiro.dto.JogoDTO;
import br.com.buzzi.campeonatobrasileiro.dto.JogoFinalizadoDTO;
import br.com.buzzi.campeonatobrasileiro.exception.JogoFinalizadoException;
import br.com.buzzi.campeonatobrasileiro.exception.JogoNaoEncontradoException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JogoResourceDoc {

    ResponseEntity<Void> gerarJogos();

    ResponseEntity<JogoDTO> findById(Long id) throws JogoNaoEncontradoException;

    ResponseEntity<List<JogoDTO>> findAll();

    ResponseEntity<List<ClassificacaoDTO>> getClassificacao();

    ResponseEntity<Void> finalizar(Long id, JogoFinalizadoDTO jogoFinalizadoDTO)
            throws JogoNaoEncontradoException, JogoFinalizadoException;

}