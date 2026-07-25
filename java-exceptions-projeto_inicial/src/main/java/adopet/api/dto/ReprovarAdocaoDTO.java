package adopet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReprovarAdocaoDTO(

        @NotBlank(message = "A justificativa da reprovação não pode estar em branco.")
        String justificativa
) {
}
