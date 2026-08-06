package adopet.api.controller.doc;

import adopet.api.dto.AdocaoRequestDTO;
import adopet.api.dto.AdocaoResponseDTO;
import adopet.api.dto.ReprovarAdocaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "Adoções", description = "Operações relacionadas ao fluxo e gerenciamento de adoções")
public interface AdocaoControllerOpenAPI {

    @Operation(summary = "Listar todas as adoções", description = "Retorna o histórico completo de todas as " +
            "adoções (pendentes, aprovadas e reprovadas).")
    @ApiResponse(responseCode = "200", description = "Adoções listadas com sucesso")
    ResponseEntity<List<AdocaoResponseDTO>> buscarTodos();

    @Operation(summary = "Buscar adoção por ID", description = "Retorna os detalhes de uma solicitação de adoção " +
            "específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adoção encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Adoção não encontrada")
    })
    ResponseEntity<AdocaoResponseDTO> buscar(Long id);

    @Operation(summary = "Solicitar adoção", description = "Abre uma nova solicitação de adoção de um pet para um tutor." +
            " Valida regras de negócio (pet já adotado, limite de adoções, etc).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitação de adoção criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Regra de negócio violada ou dados inválidos")
    })
    ResponseEntity<AdocaoResponseDTO> solicitar(AdocaoRequestDTO dados, UriComponentsBuilder uriBuilder);

    @Operation(summary = "Aprovar adoção", description = "Aprova uma solicitação de adoção que " +
            "está com status pendente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adoção aprovada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Adoção já foi processada anteriormente"),
            @ApiResponse(responseCode = "404", description = "Adoção não encontrada")
    })
    ResponseEntity<AdocaoResponseDTO> aprovar(Long id);

    @Operation(summary = "Reprovar adoção", description = "Reprova uma solicitação de adoção pendente. " +
            "Exige uma justificativa (motivo).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adoção reprovada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Justificativa inválida ou adoção já processada"),
            @ApiResponse(responseCode = "404", description = "Adoção não encontrada")
    })
    ResponseEntity<AdocaoResponseDTO> reprovar(Long id, ReprovarAdocaoDTO dto);
}
