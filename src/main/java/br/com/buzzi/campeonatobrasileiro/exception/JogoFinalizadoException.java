package br.com.buzzi.campeonatobrasileiro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class JogoFinalizadoException extends Exception {

    private static final long serialVersionUID = 1L;

    public JogoFinalizadoException(Long id) {
        super(String.format("Jogo com ID igual a %s já finalizado!", id));
    }

}
