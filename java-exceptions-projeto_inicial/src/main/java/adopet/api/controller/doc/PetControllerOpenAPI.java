package adopet.api.controller.doc;

import adopet.api.dto.PetAtualizacaoDTO;
import adopet.api.dto.PetRequestDTO;
import adopet.api.dto.PetResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "Pets", description = "Operações relacionadas ao gerenciamento de Pets")
public interface PetControllerOpenAPI {

    @Operation(summary = "Listar todos os pets", description = "Retorna uma lista com todos os pets cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Pets listados com sucesso")
    ResponseEntity<List<PetResponseDTO>> buscarTodos();

    @Operation(summary = "Listar pets disponíveis", description = "Retorna uma lista contendo apenas os pets que ainda não foram adotados.")
    @ApiResponse(responseCode = "200", description = "Pets disponíveis listados com sucesso")
    ResponseEntity<List<PetResponseDTO>> buscarPetsDisponiveis();

    @Operation(summary = "Cadastrar um novo pet", description = "Cadastra um pet com seus dados e realiza o upload da imagem.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos no formulário")
    })
    ResponseEntity<PetResponseDTO> cadastrar(
            @ParameterObject PetRequestDTO dados,
            MultipartFile imagem,
            UriComponentsBuilder uriBuilder);

    @Operation(summary = "Atualizar um pet", description = "Atualiza os dados de um pet existente. A imagem é opcional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    ResponseEntity<PetResponseDTO> atualizar(
            Long id,
            @ParameterObject PetAtualizacaoDTO dados,
            MultipartFile novaImagem);

    @Operation(summary = "Excluir um pet", description = "Exclui um pet do sistema. Retorna erro se o pet possuir histórico de adoções.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não é possível excluir pet com adoções vinculadas"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    ResponseEntity<Void> excluir(Long id);
}
