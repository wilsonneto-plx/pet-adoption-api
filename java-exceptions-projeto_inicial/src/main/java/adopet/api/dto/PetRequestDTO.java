package adopet.api.dto;

import adopet.api.model.Pet;
import adopet.api.model.TipoPet;
import adopet.api.model.TipoPorte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequestDTO(
        @NotBlank
        String nome,
        @NotNull
        Integer idade,
        @NotNull
        TipoPet tipo,
        @NotNull
        TipoPorte porte
) {

        public Pet toEntity(String nomeImagem){
                return new Pet(this.nome,this.idade(),this.tipo,this.porte, nomeImagem);


        }
}
