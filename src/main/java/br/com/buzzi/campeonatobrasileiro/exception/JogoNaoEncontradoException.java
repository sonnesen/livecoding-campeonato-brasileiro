package br.com.buzzi.campeonatobrasileiro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class JogoNaoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public JogoNaoEncontradoException(Long id) {
        super(String.format("Jogo com ID igual a %s não encontrado!", id));
    }

}
