package br.com.buzzi.campeonatobrasileiro.service;

import br.com.buzzi.campeonatobrasileiro.entity.Time;
import br.com.buzzi.campeonatobrasileiro.exception.TimeNaoEncontradoException;
import br.com.buzzi.campeonatobrasileiro.repository.TimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimeServiceTest {

    private static final Long TIME_ID_VALIDO = 1L;

    private static final Long TIME_ID_INVALIDO = 2L;

    @Mock
    private TimeRepository timeRepository;

    @InjectMocks
    private TimeService timeService;

    @Test
    void whenValidTeamIsInformedThenReturnIt() throws TimeNaoEncontradoException {
        // given
        Time time = buildTime(TIME_ID_VALIDO);

        // when
        when(timeRepository.findById(TIME_ID_VALIDO)).thenReturn(Optional.of(time));

        // then
        Time timeEsperado = timeService.findById(time.getId());
        assertThat(time, is(equalTo(timeEsperado)));
    }

    private Time buildTime(Long id) {
        return Time.builder()
                .id(id)
                .estado("SC")
                .nome("Joinville Sport Clube")
                .sigla("JEC")
                .build();
    }

    @Test
    void whenInvalidTeamIsInformedToFindThenThrowException() {
        // given
        Time time = buildTime(TIME_ID_INVALIDO);

        // when
        when(timeRepository.findById(time.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(TimeNaoEncontradoException.class, () -> timeService.findById(TIME_ID_INVALIDO));
    }

    @Test
    void whenTeamIsInformedThenItShouldBeCreated() {
        // given
        Time timeEsperado = buildTime(TIME_ID_VALIDO);

        // when
        when(timeRepository.save(timeEsperado)).thenReturn(timeEsperado);

        // then
        Time timeCriado = timeService.save(timeEsperado);
        assertThat(timeCriado, is(equalTo(timeEsperado)));
    }

    @Test
    void whenListTeamIsCalledThenReturnListOfTeams() {
        // given
        Time timeEsperado = buildTime(TIME_ID_VALIDO);

        // when
        when(timeRepository.findAll()).thenReturn(Collections.singletonList(timeEsperado));

        // then
        List<Time> times = timeService.findAll();

        assertThat(times, is(not(empty())));
        assertThat(times.get(0), is(equalTo(timeEsperado)));
    }

    @Test
    void whenListTeamIsCalledThenReturnEmptyList() {
        // when
        when(timeRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<Time> times = timeService.findAll();

        assertThat(times, is(empty()));
    }

    @Test
    void whenValidTeamIsInformedThenItShouldBeDeleted() throws TimeNaoEncontradoException {
        // given
        Time timeEsperado = buildTime(TIME_ID_VALIDO);

        // when
        when(timeRepository.findById(timeEsperado.getId())).thenReturn(Optional.of(timeEsperado));
        doNothing().when(timeRepository).deleteById(timeEsperado.getId());

        // then
        timeService.deleteById(timeEsperado.getId());

        verify(timeRepository, times(1)).findById(timeEsperado.getId());
        verify(timeRepository, times(1)).deleteById(timeEsperado.getId());
    }

    @Test
    void whenInvalidTeamIsInformedToDeleteThenThrowException() {
        // given
        Time timeEsperado = buildTime(TIME_ID_VALIDO);

        // when
        when(timeRepository.findById(timeEsperado.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(TimeNaoEncontradoException.class, () -> timeService.deleteById(timeEsperado.getId()));
    }

    @Test
    void whenValidTeamIsInformedThenItShouldBeUpdated() throws TimeNaoEncontradoException {
        // given
        Time timeParaAtualizar = Time.builder()
                .id(TIME_ID_VALIDO)
                .estado("SC")
                .nome("Joinville")
                .sigla("JEC")
                .build();

        Time timeEsperado = buildTime(TIME_ID_VALIDO);

        // when
        when(timeRepository.findById(timeEsperado.getId())).thenReturn(Optional.of(timeParaAtualizar));
        when(timeRepository.save(timeParaAtualizar)).thenReturn(timeEsperado);

        // then
        Time timeAtualizado = timeService.update(timeEsperado.getId(), timeParaAtualizar);
        assertThat(timeAtualizado, is(equalTo(timeEsperado)));
    }

    @Test
    void whenInvalidTeamIsInformedToUpdateThenThrowException() {
        // given
        Time timeParaAtualizar = Time.builder()
                .id(TIME_ID_VALIDO)
                .estado("SC")
                .nome("Joinville")
                .sigla("JEC")
                .build();

        // when
        when(timeRepository.findById(timeParaAtualizar.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(TimeNaoEncontradoException.class,
                () -> timeService.update(timeParaAtualizar.getId(), timeParaAtualizar));
    }

}
