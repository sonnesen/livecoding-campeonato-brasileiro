package br.com.buzzi.campeonatobrasileiro.resource;

import br.com.buzzi.campeonatobrasileiro.dto.TimeDTO;
import br.com.buzzi.campeonatobrasileiro.exception.TimeNaoEncontradoException;
import br.com.buzzi.campeonatobrasileiro.mapper.TimeMapper;
import br.com.buzzi.campeonatobrasileiro.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class TimeResource implements TimeResourceDoc {

    private final TimeService timeService;
    private final TimeMapper timeMapper;

    @Override
    @GetMapping
    public ResponseEntity<List<TimeDTO>> findAll() {
        return ResponseEntity.ok().body(timeMapper.toTimeDTOs(timeService.findAll()));
    }

    @Override
    @GetMapping("/{id}")
    public TimeDTO findById(@PathVariable Long id) throws TimeNaoEncontradoException {
        return timeMapper.toDTO(timeService.findById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<TimeDTO> save(@RequestBody @Valid TimeDTO timeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(timeMapper.toDTO(timeService.save(timeMapper.toModel(timeDTO))));
    }

    @Override
    @PutMapping(value = "/{id}")
    public ResponseEntity<TimeDTO> update(@RequestParam(value = "id", required = true) Long id,
                                          @Valid @RequestBody TimeDTO timeDTO) throws TimeNaoEncontradoException {
        return ResponseEntity.ok().body(timeMapper.toDTO(timeService.update(id, timeMapper.toModel(timeDTO))));
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws TimeNaoEncontradoException {
        timeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
