package br.com.buzzi.campeonatobrasileiro.service;

import br.com.buzzi.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.buzzi.campeonatobrasileiro.entity.Jogo;
import br.com.buzzi.campeonatobrasileiro.entity.Time;
import br.com.buzzi.campeonatobrasileiro.exception.JogoFinalizadoException;
import br.com.buzzi.campeonatobrasileiro.exception.JogoNaoEncontradoException;
import br.com.buzzi.campeonatobrasileiro.repository.JogoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class JogoService {

    private final JogoRepository jogoRepository;
    private final TimeService timeService;

    /**
     * @param primeiraRodada Data da primeira rodada
     */
    public void gerarJogos(LocalDateTime primeiraRodada) {
        final List<Time> times = timeService.findAll();
        List<Time> timesMandantes = new ArrayList<>();
        List<Time> timesVisitantes = new ArrayList<>();

        timesMandantes.addAll(times); // .subList(0, times.size()/2));
        timesVisitantes.addAll(times); // .subList(all1.size(), times.size()));

        jogoRepository.deleteAll();

        List<Jogo> jogos = new ArrayList<>();

        int t = times.size();
        int m = times.size() / 2;
        LocalDateTime dataJogo = primeiraRodada;
        Integer rodada = 0;
        for (int i = 0; i < t - 1; i++) {
            rodada = i + 1;
            for (int j = 0; j < m; j++) {
                // Teste para ajustar o mando de campo
                Time time1;
                Time time2;
                if (j % 2 == 1 || i % 2 == 1 && j == 0) {
                    time1 = times.get(t - j - 1);
                    time2 = times.get(j);
                } else {
                    time1 = times.get(j);
                    time2 = times.get(t - j - 1);
                }
                jogos.add(gerarJogo(dataJogo, rodada, time1, time2));
                dataJogo = dataJogo.plusDays(7);
            }
            // Gira os times no sentido horário, mantendo o primeiro no lugar
            times.add(1, times.remove(times.size() - 1));
        }

        jogoRepository.saveAll(jogos);

        List<Jogo> jogos2 = new ArrayList<>();

        jogos.forEach(jogo -> {
            Time timeMandate = jogo.getTimeVisitante();
            Time timeVisitante = jogo.getTimeMandante();
            jogos2.add(gerarJogo(jogo.getData().plusDays(7 * jogos.size()), jogo.getRodada() + jogos.size(), timeMandate,
                    timeVisitante));
        });

        jogoRepository.saveAll(jogos2);
    }

    private Jogo gerarJogo(LocalDateTime dataJogo, Integer rodada, Time timeMandante, Time timeVisitante) {
        Jogo jogo = Jogo.builder()
                .timeMandante(timeMandante)
                .timeVisitante(timeVisitante)
                .rodada(rodada)
                .data(dataJogo)
                .encerrado(false)
                .golsTimeMandante(0)
                .golsTimeVisitante(0)
                .publicoPagante(0)
                .build();
        return jogo;
    }

    public Jogo findById(Long id) throws JogoNaoEncontradoException {
        return jogoRepository.findById(id).orElseThrow(() -> new JogoNaoEncontradoException(id));
    }

    public List<Jogo> findAll() {
        return jogoRepository.findAll();
    }

    public List<ClassificacaoDTO> getClassificacao() {
        List<ClassificacaoDTO> lista = new ArrayList<>();

        final List<Time> times = timeService.findAll();
        times.forEach(time -> {
            final List<Jogo> jogosComoMandante = jogoRepository.findByTimeMandanteAndEncerrado(time, true);
            final List<Jogo> jogosComoVisitante = jogoRepository.findByTimeVisitanteAndEncerrado(time, true);
            AtomicReference<Integer> vitorias = new AtomicReference<>(0);
            AtomicReference<Integer> empates = new AtomicReference<>(0);
            AtomicReference<Integer> derrotas = new AtomicReference<>(0);
            AtomicReference<Integer> golsMarcados = new AtomicReference<>(0);
            AtomicReference<Integer> golsSofridos = new AtomicReference<>(0);

            jogosComoMandante.forEach(jogo -> {
                if (jogo.getGolsTimeMandante() > jogo.getGolsTimeVisitante()) {
                    vitorias.getAndSet(vitorias.get() + 1);
                } else if (jogo.getGolsTimeMandante() < jogo.getGolsTimeVisitante()) {
                    derrotas.getAndSet(derrotas.get() + 1);
                } else {
                    empates.getAndSet(empates.get() + 1);
                }
                golsMarcados.set(golsMarcados.get() + jogo.getGolsTimeMandante());
                golsSofridos.getAndSet(golsSofridos.get() + jogo.getGolsTimeVisitante());
            });

            jogosComoVisitante.forEach(jogo -> {
                if (jogo.getGolsTimeVisitante() > jogo.getGolsTimeMandante()) {
                    vitorias.getAndSet(vitorias.get() + 1);
                } else if (jogo.getGolsTimeVisitante() < jogo.getGolsTimeMandante()) {
                    derrotas.getAndSet(derrotas.get() + 1);
                } else {
                    empates.getAndSet(empates.get() + 1);
                }
                golsMarcados.set(golsMarcados.get() + jogo.getGolsTimeVisitante());
                golsSofridos.getAndSet(golsSofridos.get() + jogo.getGolsTimeMandante());
            });

            ClassificacaoDTO classificacaoTimeDto = ClassificacaoDTO.builder()
                    .time(time.getNome())
                    .pontos((vitorias.get() * 3) + empates.get())
                    .derrotas(derrotas.get())
                    .empates(empates.get())
                    .vitorias(vitorias.get())
                    .golsMarcados(golsMarcados.get())
                    .golsSofridos(golsSofridos.get())
                    .jogos(derrotas.get() + empates.get() + vitorias.get())
                    .build();

            lista.add(classificacaoTimeDto);
        });

        Collections.sort(lista, Collections.reverseOrder());
        int posicao = 0;
        for (ClassificacaoDTO time : lista) {
            time.setPosicao(posicao++);
        }

        return lista;
    }

    public Jogo finalizar(Long id, Jogo jogoParaFinalizar) throws JogoNaoEncontradoException, JogoFinalizadoException {
        Jogo jogo = jogoRepository.findById(id).orElseThrow(() -> new JogoNaoEncontradoException(id));

        if (jogo.getEncerrado()) {
            throw new JogoFinalizadoException(id);
        }

        jogo.setGolsTimeMandante(jogoParaFinalizar.getGolsTimeMandante());
        jogo.setGolsTimeVisitante(jogoParaFinalizar.getGolsTimeVisitante());
        jogo.setEncerrado(true);
        jogo.setPublicoPagante(jogoParaFinalizar.getPublicoPagante());

        return jogoRepository.save(jogo);
    }
}
