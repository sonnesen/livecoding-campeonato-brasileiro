package br.com.buzzi.campeonatobrasileiro.mapper;

import br.com.buzzi.campeonatobrasileiro.dto.JogoDTO;
import br.com.buzzi.campeonatobrasileiro.dto.JogoFinalizadoDTO;
import br.com.buzzi.campeonatobrasileiro.entity.Jogo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JogoMapper {

    Jogo toModel(JogoDTO jogoDTO);

    Jogo toModel(JogoFinalizadoDTO jogoFinalizadoDTO);

    JogoDTO toDTO(Jogo jogo);

    List<JogoDTO> toJogoDTOs(List<Jogo> jogos);
}
