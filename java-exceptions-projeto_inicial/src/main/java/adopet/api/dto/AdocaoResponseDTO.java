package adopet.api.dto;

import adopet.api.model.Adocao;
import adopet.api.model.StatusAdocao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public record AdocaoResponseDTO(Long idAdocao,
                                Long idTutor,
                                Long idPet,
                                String motivo,
                                StatusAdocao status,

                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                String justificativa,

                                @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                LocalDateTime dataSolicitacao) {

    public AdocaoResponseDTO(Adocao adocao){
        this(   adocao.getId(),
                adocao.getTutor().getId(),
                adocao.getPet().getId(),
                adocao.getMotivo(),
                adocao.getStatus(),

                adocao.getStatus() == StatusAdocao.REPROVADO ? adocao.getJustificativa() : null,

                adocao.getDataSolicitacao() != null ? adocao.getDataSolicitacao() : LocalDateTime.now()
        );
    }
}
