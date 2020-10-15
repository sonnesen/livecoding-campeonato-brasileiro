package br.com.buzzi.campeonatobrasileiro.service;

import br.com.buzzi.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.buzzi.campeonatobrasileiro.entity.Jogo;
import br.com.buzzi.campeonatobrasileiro.entity.Time;
import br.com.buzzi.campeonatobrasileiro.exception.JogoFinalizadoException;
import br.com.buzzi.campeonatobrasileiro.exception.JogoNaoEncontradoException;
import br.com.buzzi.campeonatobrasileiro.repository.JogoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JogoServiceTest {

    private static final Long JOGO_ID_VALIDO = 1L;

    private static final Long JOGO_ID_INVALIDO = 2L;

    @Mock
    private JogoRepository jogoRepository;

    @Mock
    private TimeService timeService;

    @InjectMocks
    private JogoService jogoService;

    @Test
    void whenValidIdGameIsInformedThenReturnIt() throws JogoNaoEncontradoException {
        // given
        Jogo jogo = buildJogo(JOGO_ID_VALIDO);

        // when
        when(jogoRepository.findById(JOGO_ID_VALIDO)).thenReturn(Optional.of(jogo));

        // then
        Jogo jogoEsperado = jogoService.findById(jogo.getId());
        assertThat(jogo, is(equalTo(jogoEsperado)));
    }

    private Jogo buildJogo(Long id) {
        return Jogo.builder()
                .data(LocalDateTime.of(2020, 10, 14, 21, 00))
                .encerrado(false)
                .golsTimeMandante(0)
                .golsTimeVisitante(0)
                .id(id)
                .publicoPagante(0)
                .rodada(0)
                .timeMandante(buildTimeMandate())
                .timeVisitante(buildTimeVisitante())
                .build();
    }

    private Time buildTimeVisitante() {
        return Time.builder()
                .estado("SC")
                .id(2L)
                .nome("Criciúma Sport Clube")
                .sigla("CEC")
                .build();
    }

    private Time buildTimeMandate() {
        return Time.builder()
                .estado("SC")
                .id(1L)
                .nome("Joinville Sport Clube")
                .sigla("JEC")
                .build();
    }

    @Test
    void whenInvalidGameIsInformedToFindThenThrowException() {
        // given
        Jogo jogo = buildJogo(JOGO_ID_INVALIDO);

        // when
        when(jogoRepository.findById(jogo.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(JogoNaoEncontradoException.class,
                () -> jogoService.findById(JOGO_ID_INVALIDO));
    }

    @Test
    void whenListGameIsCalledThenReturnListOfGames() {
        // given
        Jogo jogoEsperado = buildJogo(JOGO_ID_VALIDO);

        // when
        when(jogoRepository.findAll()).thenReturn(Collections.singletonList(jogoEsperado));

        // then
        List<Jogo> jogos = jogoService.findAll();

        assertThat(jogos, is(not(empty())));
        assertThat(jogos.get(0), is(equalTo(jogoEsperado)));
    }

    @Test
    void whenListGameIsCalledThenReturnEmptyList() {
        // when
        when(jogoRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<Jogo> jogos = jogoService.findAll();

        assertThat(jogos, is(empty()));
    }

    @Test
    void whenValidGameIsInformedThenShouldBeFinalized()
            throws JogoNaoEncontradoException, JogoFinalizadoException {
        // given
        Jogo jogoParaFinalizar = buildJogo(JOGO_ID_VALIDO);
        Jogo jogoEsperado = buildJogo(JOGO_ID_VALIDO);
        jogoEsperado.setEncerrado(true);

        // when
        when(jogoRepository.findById(jogoParaFinalizar.getId()))
                .thenReturn(Optional.of(jogoParaFinalizar));
        when(jogoRepository.save(jogoParaFinalizar)).thenReturn(jogoEsperado);

        // then
        Jogo jogoFinalizado = jogoService.finalizar(jogoParaFinalizar.getId(), jogoParaFinalizar);

        assertThat(jogoFinalizado, is(equalTo(jogoParaFinalizar)));
    }

    @Test
    void whenInvalidGameIsInformedToFinalizeThenThrowException()
            throws JogoNaoEncontradoException, JogoFinalizadoException {
        // given
        Jogo jogoParaFinalizar = buildJogo(JOGO_ID_VALIDO);

        // when
        when(jogoRepository.findById(jogoParaFinalizar.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(JogoNaoEncontradoException.class,
                () -> jogoService.finalizar(jogoParaFinalizar.getId(), jogoParaFinalizar));
    }

    @Test
    void whenFinalizedGameIsInformedToFinalizeThenThrowException()
            throws JogoNaoEncontradoException, JogoFinalizadoException {
        // given
        Jogo jogoParaFinalizar = buildJogo(JOGO_ID_VALIDO);
        Jogo jogoFinalizado = buildJogo(JOGO_ID_VALIDO);
        jogoFinalizado.setEncerrado(true);

        // when
        when(jogoRepository.findById(jogoParaFinalizar.getId()))
                .thenReturn(Optional.of(jogoFinalizado));

        // then
        assertThrows(JogoFinalizadoException.class,
                () -> jogoService.finalizar(jogoParaFinalizar.getId(), jogoParaFinalizar));
    }

    @Test
    void whenClassificationListIsCalledThenReturnListOfClassifications() {
        // given
        ClassificacaoDTO classificacao = ClassificacaoDTO.builder()
                .derrotas(0)
                .empates(0)
                .golsMarcados(3)
                .golsSofridos(1)
                .jogos(2)
                .pontos(6)
                .posicao(0)
                .time("Joinville Sport Clube")
                .vitorias(2)
                .build();

        Time joinville = buildTimeMandate();

        Time criciuma = buildTimeVisitante();

        Jogo jogo1 = Jogo.builder()
                .encerrado(true)
                .golsTimeMandante(2)
                .golsTimeVisitante(1)
                .timeMandante(joinville)
                .timeVisitante(criciuma)
                .build();

        Jogo jogo2 = Jogo.builder()
                .encerrado(true)
                .golsTimeMandante(0)
                .golsTimeVisitante(1)
                .timeMandante(criciuma)
                .timeVisitante(joinville)
                .build();

        // when
        when(timeService.findAll()).thenReturn(Collections.singletonList(joinville));
        when(jogoRepository.findByTimeMandanteAndEncerrado(joinville, true))
                .thenReturn(Collections.singletonList(jogo1));
        when(jogoRepository.findByTimeVisitanteAndEncerrado(joinville, true))
                .thenReturn(Collections.singletonList(jogo2));

        // then
        List<ClassificacaoDTO> classificacoes = jogoService.getClassificacao();
        assertThat(classificacoes, is(not(empty())));
        assertThat(classificacoes.get(0), is(equalTo(classificacao)));
    }

    @Test
    void whenGameGenerationIsCalledThenGenerateGames() {
        // given
        List<Time> times = new ArrayList<>();
        times.add(new Time(1L, "Associação Chapecoense de Futebol", "SC", "ACF"));
        times.add(new Time(2L, "Avaí Futebol Clube", "SC", "AFC"));
        times.add(new Time(3L, "Brusque Futebol Clube", "SC", "BFC"));
        times.add(new Time(4L, "Criciúma Futebol Clube", "SC", "CFC"));
        times.add(new Time(5L, "Figueirense Futebol Clube", "SC", "FFC"));
        times.add(new Time(6L, "Joinville Esporte Clube", "SC", "JEC"));
        times.add(new Time(7L, "Clube Atlético Tubarão", "SC", "CAT"));
        times.add(new Time(8L, "Hercílio Luz Futebol Clube", "SC", "HLFC"));

        // when
        when(timeService.findAll()).thenReturn(times);

        // then
        jogoService.gerarJogos(LocalDateTime.now());
    }

}
