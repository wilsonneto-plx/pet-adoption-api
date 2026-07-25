package adopet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdocaoRequestDTO(

        @NotNull(message = "O ID do pet é obrigatório.")
        Long idPet,

        @NotNull(message = "O ID do tutor é obrigatório.")
        Long idTutor,

        @NotBlank(message = "O motivo da adoção não pode estar em branco.")
        String motivo
) {
}
