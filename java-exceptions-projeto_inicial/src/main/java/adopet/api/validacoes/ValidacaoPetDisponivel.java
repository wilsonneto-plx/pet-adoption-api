package adopet.api.validacoes;

import adopet.api.dto.AdocaoRequestDTO;
import adopet.api.exception.AdocaoException;
import adopet.api.model.Pet;
import adopet.api.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidacaoPetDisponivel implements ValidacaoSolicitacaoAdocao {

    private final PetRepository petRepository;

    public void validar(AdocaoRequestDTO dto) {

        Pet pet = petRepository.findById(dto.idPet())
                .orElseThrow(() -> new AdocaoException("Solicitação falhou: Pet não encontrado."));

        if(pet.getAdotado()) {
            throw new AdocaoException("Pet já adotado");
        }
    }
}
