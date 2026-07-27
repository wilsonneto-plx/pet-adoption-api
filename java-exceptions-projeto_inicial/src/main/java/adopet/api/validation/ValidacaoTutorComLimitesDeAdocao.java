package adopet.api.validation;

import adopet.api.dto.AdocaoRequestDTO;
import adopet.api.exception.AdocaoException;
import adopet.api.model.StatusAdocao;
import adopet.api.repository.AdocaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidacaoTutorComLimitesDeAdocao implements ValidacaoSolicitacaoAdocao {

    private final AdocaoRepository adocaoRepository;

    public void validar(AdocaoRequestDTO dto) {

        Integer tutorAdocoes = adocaoRepository.countByTutorIdAndStatus(dto.idTutor(), StatusAdocao.APROVADO);
        if(tutorAdocoes >= 2) {
            throw new AdocaoException("Tutor com máximo de adoções permitido.");
        }

    }
}
