package adopet.api.dto;

import adopet.api.model.Tutor;

public record TutorResponseDTO(Long id, String nome, String email, String telefone) {

    public TutorResponseDTO(Tutor tutor){
        this(   tutor.getId(),
                tutor.getNome(),
                tutor.getEmail(),
                tutor.getTelefone() != null ? tutor.getTelefone() : "Não informado"
        );
    }
}
