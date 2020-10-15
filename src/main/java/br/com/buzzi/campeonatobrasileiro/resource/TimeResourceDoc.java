package br.com.buzzi.campeonatobrasileiro.resource;

import br.com.buzzi.campeonatobrasileiro.dto.TimeDTO;
import br.com.buzzi.campeonatobrasileiro.exception.TimeNaoEncontradoException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api("Gerencia os time de futebol")
public interface TimeResourceDoc {

    @ApiOperation(value = "Retorna lista dos times")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista dos times cadastrados")
    })
    ResponseEntity<List<TimeDTO>> findAll();

    @ApiOperation(value = "Retorna um time por meio do identicador")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Time encontrado"),
            @ApiResponse(code = 404, message = "Time não encontrado")
    })
    TimeDTO findById(Long id) throws TimeNaoEncontradoException;

    @ApiOperation(value = "Cria um time")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Time criado com sucesso"),
            @ApiResponse(code = 400, message = "Campos obrigatórios não informados ou incorretos")
    })
    ResponseEntity<TimeDTO> save(TimeDTO timeDTO);

    @ApiOperation(value = "Atualiza um time")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Time atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Campos obrigatórios não informados ou incorretos")
    })
    ResponseEntity<TimeDTO> update(Long id, TimeDTO timeDTO) throws TimeNaoEncontradoException;

    @ApiOperation(value = "Remove um time")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Time removido com sucesso"),
            @ApiResponse(code = 400, message = "Campos obrigatórios não informados ou incorretos")
    })
    ResponseEntity<Void> deleteById(Long id) throws TimeNaoEncontradoException;

}