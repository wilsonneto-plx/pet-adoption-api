package adopet.api.controller.doc;

import adopet.api.dto.TutorAtualizacaoDTO;
import adopet.api.dto.TutorRequestDTO;
import adopet.api.dto.TutorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "Tutores", description = "Operações relacionadas ao gerenciamento de Tutores")
public interface TutorControllerOpenAPI {

    @Operation(summary = "Listar todos os tutores", description = "Retorna uma lista com todos os tutores cadastrados " +
            "no sistema.")
    @ApiResponse(responseCode = "200", description = "Tutores listados com sucesso")
    ResponseEntity<List<TutorResponseDTO>> buscarTodos();

    @Operation(summary = "Cadastrar um novo tutor", description = "Cadastra um tutor na plataforma. " +
            "O e-mail deve ser único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tutor cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já cadastrado")
    })
    ResponseEntity<TutorResponseDTO> cadastrar(TutorRequestDTO dados, UriComponentsBuilder uriBuilder);

    @Operation(summary = "Atualizar um tutor", description = "Atualiza os dados de contato de um tutor existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado")
    })
    ResponseEntity<TutorResponseDTO> atualizar(Long id, TutorAtualizacaoDTO dados);

    @Operation(summary = "Excluir um tutor", description = "Exclui um tutor do sistema. " +
            "Retorna erro se o tutor possuir histórico de adoções.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tutor excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não é possível excluir tutor com adoções vinculadas"),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado")
    })
    ResponseEntity<Void> excluir(Long id);
}