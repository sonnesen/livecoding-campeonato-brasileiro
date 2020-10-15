package br.com.buzzi.campeonatobrasileiro.resource;

import br.com.buzzi.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.buzzi.campeonatobrasileiro.dto.JogoDTO;
import br.com.buzzi.campeonatobrasileiro.dto.JogoFinalizadoDTO;
import br.com.buzzi.campeonatobrasileiro.dto.TimeDTO;
import br.com.buzzi.campeonatobrasileiro.exception.JogoFinalizadoException;
import br.com.buzzi.campeonatobrasileiro.exception.JogoNaoEncontradoException;
import br.com.buzzi.campeonatobrasileiro.mapper.JogoMapper;
import br.com.buzzi.campeonatobrasileiro.service.JogoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static br.com.buzzi.campeonatobrasileiro.utils.JsonConverter.asJsonString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class JogoResourceTest {

    private static final String URL_JOGOS = "/jogos";

    private static final Long JOGO_ID_VALIDO = 1L;

    private static final Long JOGO_ID_INVALIDO = 99L;

    private MockMvc mockMvc;

    @Mock
    private JogoService jogoService;

    @InjectMocks
    private JogoResource jogoResource;

    @Mock
    private JogoMapper jogoMapper;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(jogoResource)
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenGETisCalledWithValidIDThenOkStatusIsReturned() throws Exception {
        // given
        JogoDTO jogoDTO = buildJogo();

        // when
        when(jogoMapper.toDTO(jogoService.findById(JOGO_ID_VALIDO))).thenReturn(jogoDTO);

        // then
        mockMvc.perform(get(URL_JOGOS + "/" + JOGO_ID_VALIDO).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data",
                        is(jogoDTO.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.encerrado", is(equalTo(jogoDTO.getEncerrado()))))
                .andExpect(jsonPath("$.golsTimeMandante", is(equalTo(jogoDTO.getGolsTimeMandante()))))
                .andExpect(jsonPath("$.golsTimeVisitante", is(equalTo(jogoDTO.getGolsTimeVisitante()))))
                .andExpect(jsonPath("$.publicoPagante", is(equalTo(jogoDTO.getPublicoPagante()))))
                .andExpect(jsonPath("$.rodada", is(equalTo(jogoDTO.getRodada()))))
                .andExpect(jsonPath("$.timeMandante.nome", is(equalTo(jogoDTO.getTimeMandante().getNome()))))
                .andExpect(jsonPath("$.timeVisitante.nome", is(equalTo(jogoDTO.getTimeVisitante().getNome()))));
    }

    @Test
    void whenGETisCalledWithInvalidGameThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(jogoService.findById(JOGO_ID_INVALIDO)).thenThrow(JogoNaoEncontradoException.class);

        // then
        mockMvc.perform(get(URL_JOGOS + "/" + JOGO_ID_INVALIDO).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithGamesIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        JogoDTO jogoDTO = buildJogo();

        // when
        when(jogoMapper.toJogoDTOs(jogoService.findAll())).thenReturn(Collections.singletonList(jogoDTO));

        // then
        mockMvc.perform(get(URL_JOGOS).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].data",
                        is(jogoDTO.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].encerrado", is(equalTo(jogoDTO.getEncerrado()))))
                .andExpect(jsonPath("$[0].golsTimeMandante", is(equalTo(jogoDTO.getGolsTimeMandante()))))
                .andExpect(jsonPath("$[0].golsTimeVisitante", is(equalTo(jogoDTO.getGolsTimeVisitante()))))
                .andExpect(jsonPath("$[0].publicoPagante", is(equalTo(jogoDTO.getPublicoPagante()))))
                .andExpect(jsonPath("$[0].rodada", is(equalTo(jogoDTO.getRodada()))))
                .andExpect(jsonPath("$[0].timeMandante.nome", is(equalTo(jogoDTO.getTimeMandante().getNome()))))
                .andExpect(jsonPath("$[0].timeVisitante.nome", is(equalTo(jogoDTO.getTimeVisitante().getNome()))));

    }

    private JogoDTO buildJogo() {
        TimeDTO timeMandante = TimeDTO.builder()
                .nome("Associação Chapecoense de Futebol")
                .sigla("ACF")
                .estado("SC")
                .build();

        TimeDTO timeVisitante = TimeDTO.builder()
                .nome("Brusque Futebol Clube")
                .sigla("BFC")
                .estado("SC")
                .build();

        JogoDTO jogoDTO = JogoDTO.builder()
                .data(LocalDateTime.of(2020, 10, 14, 21, 0, 0))
                .encerrado(false)
                .golsTimeMandante(0)
                .golsTimeVisitante(0)
                .publicoPagante(0)
                .rodada(0)
                .timeMandante(timeMandante)
                .timeVisitante(timeVisitante)
                .build();

        return jogoDTO;
    }

    @Test
    void whenGETListWithoutGamesIsCalledThenOkStatusIsReturned() throws Exception {
        // when
        when(jogoService.findAll()).thenReturn(Collections.emptyList());

        // then
        mockMvc.perform(get(URL_JOGOS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPOSTisCalledWithValidGameThenFinalizeGame() throws Exception {
        // given
        JogoFinalizadoDTO jogoParaFinalizar = JogoFinalizadoDTO.builder()
                .golsTimeMandante(2)
                .golsTimeVisitante(1)
                .publicoPagante(20_000)
                .build();

        // then
        mockMvc.perform(post(URL_JOGOS + "/" + JOGO_ID_VALIDO + "/finalizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(jogoParaFinalizar)))
                .andExpect(status().isOk());
    }

    @Test
    void whenPOSTisCalledWithFinalizedGameThenThrowException() throws Exception {
        // given
        JogoFinalizadoDTO jogoParaFinalizar = JogoFinalizadoDTO.builder()
                .golsTimeMandante(2)
                .golsTimeVisitante(1)
                .publicoPagante(20_000)
                .build();

        // when
        when(jogoService.finalizar(JOGO_ID_VALIDO, jogoMapper.toModel(jogoParaFinalizar)))
                .thenThrow(JogoFinalizadoException.class);

        // then
        mockMvc.perform(post(URL_JOGOS + "/" + JOGO_ID_VALIDO + "/finalizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(jogoParaFinalizar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPOSTisCalledWithInvalidGameThenThrowException() throws Exception {
        // given
        JogoFinalizadoDTO jogoParaFinalizar = JogoFinalizadoDTO.builder()
                .golsTimeMandante(2)
                .golsTimeVisitante(1)
                .publicoPagante(20_000)
                .build();

        // when
        when(jogoService.finalizar(JOGO_ID_VALIDO, jogoMapper.toModel(jogoParaFinalizar)))
                .thenThrow(JogoNaoEncontradoException.class);

        // then
        mockMvc.perform(post(URL_JOGOS + "/" + JOGO_ID_VALIDO + "/finalizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(jogoParaFinalizar)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPOSTisCalledThenReturnListOfClassifications() throws Exception {
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

        // when
        when(jogoService.getClassificacao()).thenReturn(Collections.singletonList(classificacao));

        // then
        mockMvc.perform(get(URL_JOGOS + "/classificacao")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", is(equalTo(classificacao.getTime()))))
                .andExpect(jsonPath("$[0].posicao", is(equalTo(classificacao.getPosicao()))))
                .andExpect(jsonPath("$[0].pontos", is(equalTo(classificacao.getPontos()))))
                .andExpect(jsonPath("$[0].jogos", is(equalTo(classificacao.getJogos()))))
                .andExpect(jsonPath("$[0].vitorias", is(equalTo(classificacao.getVitorias()))))
                .andExpect(jsonPath("$[0].empates", is(equalTo(classificacao.getEmpates()))))
                .andExpect(jsonPath("$[0].derrotas", is(equalTo(classificacao.getDerrotas()))))
                .andExpect(jsonPath("$[0].golsMarcados", is(equalTo(classificacao.getGolsMarcados()))))
                .andExpect(jsonPath("$[0].golsSofridos", is(equalTo(classificacao.getGolsSofridos()))));
    }

    @Test
    void whenPOSTisCalledThenGenerateGames() throws Exception {
        // when
        // doNothing().when(jogoService).gerarJogos(LocalDateTime.of(2020,10,14,21,0,0));

        // then
        mockMvc.perform(post(URL_JOGOS))
                .andExpect(status().isOk());
    }

}
