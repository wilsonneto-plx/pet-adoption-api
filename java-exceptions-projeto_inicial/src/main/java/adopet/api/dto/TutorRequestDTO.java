package adopet.api.dto;

import adopet.api.model.Tutor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TutorRequestDTO(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "\\(?\\d{2}\\)?\\d{4}-?\\d{4}")
        String telefone
) {

        public Tutor toEntity() {
                return new Tutor(this.nome, this.email, this.telefone);
        }
}
