package adopet.api.validation;

import adopet.api.dto.AdocaoRequestDTO;

public interface ValidacaoSolicitacaoAdocao {

    void validar(AdocaoRequestDTO dto);
}
