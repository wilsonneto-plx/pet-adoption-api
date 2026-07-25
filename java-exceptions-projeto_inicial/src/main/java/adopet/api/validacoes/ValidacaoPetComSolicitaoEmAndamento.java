package adopet.api.validacoes;

import adopet.api.dto.AdocaoRequestDTO;
import adopet.api.exception.AdocaoException;
import adopet.api.model.StatusAdocao;
import adopet.api.repository.AdocaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidacaoPetComSolicitaoEmAndamento implements ValidacaoSolicitacaoAdocao{

    private final AdocaoRepository adocaoRepository;

    public void validar(AdocaoRequestDTO dto) {

        boolean petAdocaoEmAndamento = adocaoRepository
                .existsByPetIdAndStatus(dto.idPet(), StatusAdocao.AGUARDANDO_AVALIACAO);

        if(petAdocaoEmAndamento) {
            throw new AdocaoException(("Pet com adoção em andamento."));
        }
    }

}
