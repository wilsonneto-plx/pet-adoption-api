package adopet.api.validacoes;

import adopet.api.dto.AdocaoRequestDTO;

public interface ValidacaoSolicitacaoAdocao {

    void validar(AdocaoRequestDTO dto);
}
