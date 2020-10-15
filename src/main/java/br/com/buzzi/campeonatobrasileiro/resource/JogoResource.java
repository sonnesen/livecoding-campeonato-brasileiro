package br.com.buzzi.campeonatobrasileiro.resource;

import br.com.buzzi.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.buzzi.campeonatobrasileiro.dto.JogoDTO;
import br.com.buzzi.campeonatobrasileiro.dto.JogoFinalizadoDTO;
import br.com.buzzi.campeonatobrasileiro.exception.JogoFinalizadoException;
import br.com.buzzi.campeonatobrasileiro.exception.JogoNaoEncontradoException;
import br.com.buzzi.campeonatobrasileiro.mapper.JogoMapper;
import br.com.buzzi.campeonatobrasileiro.service.JogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/jogos")
@RequiredArgsConstructor
public class JogoResource implements JogoResourceDoc {

    private final JogoService jogoService;
    private final JogoMapper jogoMapper;

    @Override
    @PostMapping
    public ResponseEntity<Void> gerarJogos() {
        jogoService.gerarJogos(LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<JogoDTO> findById(@PathVariable Long id) throws JogoNaoEncontradoException {
        return ResponseEntity.ok().body(jogoMapper.toDTO(jogoService.findById(id)));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<JogoDTO>> findAll() {
        return ResponseEntity.ok().body(jogoMapper.toJogoDTOs(jogoService.findAll()));
    }

    @Override
    @GetMapping(value = "/classificacao")
    public ResponseEntity<List<ClassificacaoDTO>> getClassificacao() {
        return ResponseEntity.ok().body(jogoService.getClassificacao());
    }

    @Override
    @PostMapping(value = "/{id}/finalizar")
    public ResponseEntity<Void> finalizar(@PathVariable Long id, @RequestBody @Valid JogoFinalizadoDTO jogoFinalizadoDTO)
            throws JogoNaoEncontradoException, JogoFinalizadoException {
        jogoService.finalizar(id, jogoMapper.toModel(jogoFinalizadoDTO));
        return ResponseEntity.ok().build();
    }
}
