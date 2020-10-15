package br.com.buzzi.campeonatobrasileiro.resource;

import br.com.buzzi.campeonatobrasileiro.dto.TimeDTO;
import br.com.buzzi.campeonatobrasileiro.exception.TimeNaoEncontradoException;
import br.com.buzzi.campeonatobrasileiro.mapper.TimeMapper;
import br.com.buzzi.campeonatobrasileiro.service.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TimeResourceTest {

    private static final String URL_TIMES = "/times";

    private static final Long TIME_ID_VALIDO = 1L;

    private static final Long TIME_ID_INVALIDO = 99L;

    private MockMvc mockMvc;

    @Mock
    private TimeService timeService;

    @InjectMocks
    private TimeResource timeResource;

    @Mock
    private TimeMapper timeMapper;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(timeResource)
                .build();
    }

    @Test
    void whenGETisCalledWithValidIDThenOkStatusIsReturned() throws Exception {
        // given
        TimeDTO timeDTO = TimeDTO.builder()
                .estado("SC")
                .nome("Joinville Sport Clube")
                .sigla("JEC")
                .build();

        // when
        when(timeMapper.toDTO(timeService.findById(TIME_ID_VALIDO))).thenReturn(timeDTO);

        // then
        mockMvc.perform(get(URL_TIMES + "/" + TIME_ID_VALIDO).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(timeDTO.getNome())))
                .andExpect(jsonPath("$.sigla", is(timeDTO.getSigla())))
                .andExpect(jsonPath("$.estado", is(timeDTO.getEstado())));
    }

    @Test
    void whenGETisCalledWithInvalidIDThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(timeService.findById(TIME_ID_INVALIDO)).thenThrow(TimeNaoEncontradoException.class);
        // then
        mockMvc.perform(get(URL_TIMES + "/" + TIME_ID_INVALIDO).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithTeamsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        TimeDTO timeDTO = TimeDTO.builder()
                .estado("SC")
                .nome("Joinville Sport Clube")
                .sigla("JEC")
                .build();

        // when
        when(timeMapper.toTimeDTOs(timeService.findAll())).thenReturn(Collections.singletonList(timeDTO));

        // then
        mockMvc.perform(get(URL_TIMES).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is(timeDTO.getNome())))
                .andExpect(jsonPath("$[0].sigla", is(timeDTO.getSigla())))
                .andExpect(jsonPath("$[0].estado", is(timeDTO.getEstado())));

    }

    @Test
    void whenGETListWithoutTeamsIsCalledThenOkStatusIsReturned() throws Exception {
        // when
        when(timeService.findAll()).thenReturn(Collections.emptyList());

        // then
        mockMvc.perform(get(URL_TIMES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
