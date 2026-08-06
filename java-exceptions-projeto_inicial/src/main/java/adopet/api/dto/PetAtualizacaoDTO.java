package adopet.api.dto;

import adopet.api.model.TipoPorte;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record PetAtualizacaoDTO(

        @Size(min = 2, message = "Se informado, o nome deve ter pelo menos 2 caracteres.")
        String nome,

        TipoPorte porte,

        @PositiveOrZero(message = "Se informada, a idade não pode ser negativa.")
        Integer idade

) {
}
