package br.com.buzzi.campeonatobrasileiro.service;

import br.com.buzzi.campeonatobrasileiro.entity.Time;
import br.com.buzzi.campeonatobrasileiro.exception.TimeNaoEncontradoException;
import br.com.buzzi.campeonatobrasileiro.repository.TimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository timeRepository;

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public Time save(Time time) {
        return timeRepository.save(time);
    }

    public Time update(Long id, Time timeToUpdate) throws TimeNaoEncontradoException {
        timeRepository.findById(id).orElseThrow(() -> new TimeNaoEncontradoException(id));
        timeToUpdate.setId(id);
        return timeRepository.save(timeToUpdate);
    }

    public Time findById(Long id) throws TimeNaoEncontradoException {
        return timeRepository.findById(id).orElseThrow(() -> new TimeNaoEncontradoException(id));
    }

    public void deleteById(Long id) throws TimeNaoEncontradoException {
        timeRepository.findById(id).orElseThrow(() -> new TimeNaoEncontradoException(id));
        timeRepository.deleteById(id);
    }
}
