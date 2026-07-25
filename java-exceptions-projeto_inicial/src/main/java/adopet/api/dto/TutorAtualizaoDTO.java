package adopet.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record TutorAtualizaoDTO(
        String nome,

        @Email
        String email,

        @Pattern(regexp = "\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}", message = "Formato de telefone inválido")
        String telefone
) {
}
