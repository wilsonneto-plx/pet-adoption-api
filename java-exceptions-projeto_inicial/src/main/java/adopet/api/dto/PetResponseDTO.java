package adopet.api.dto;

import adopet.api.model.Pet;
import adopet.api.model.TipoPet;
import adopet.api.model.TipoPorte;

public record PetResponseDTO(Long id, String nome, Integer idade, TipoPet tipo, TipoPorte porte, Boolean adotado, String imagem) {

    public PetResponseDTO(Pet pet){
        this(pet.getId(),
                pet.getNome(),
                pet.getIdade(),
                pet.getTipo(),
                pet.getPorte() != null ? pet.getPorte() : TipoPorte.NAO_INFORMADO,
                pet.getAdotado(),
                pet.getImagem());

    }
}
