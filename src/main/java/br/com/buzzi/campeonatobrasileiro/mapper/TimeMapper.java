package br.com.buzzi.campeonatobrasileiro.mapper;

import br.com.buzzi.campeonatobrasileiro.dto.TimeDTO;
import br.com.buzzi.campeonatobrasileiro.entity.Time;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeMapper {

    Time toModel(TimeDTO timeDTO);

    TimeDTO toDTO(Time time);

    List<TimeDTO> toTimeDTOs(List<Time> times);
}
