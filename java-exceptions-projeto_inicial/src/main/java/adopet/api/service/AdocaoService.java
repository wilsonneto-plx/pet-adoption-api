package adopet.api.service;

import adopet.api.dto.*;
import adopet.api.exception.AdocaoException;
import adopet.api.model.Adocao;
import adopet.api.model.Pet;
import adopet.api.model.StatusAdocao;
import adopet.api.model.Tutor;
import adopet.api.repository.AdocaoRepository;
import adopet.api.repository.PetRepository;
import adopet.api.repository.TutorRepository;
import adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdocaoService {

    private final PetRepository petRepository;

    private final TutorRepository tutorRepository;

    private final AdocaoRepository adocaoRepository;

    private final List<ValidacaoSolicitacaoAdocao> validacoes;

    public List<AdocaoResponseDTO> listarTodos(){

        return adocaoRepository.findAll().stream().map(AdocaoResponseDTO::new).toList();
    }
    public AdocaoResponseDTO listar(Long id){

        return adocaoRepository.findById(id).map(AdocaoResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Adoção não encontrada no banco"));
    }

    @Transactional
    public AdocaoResponseDTO solicitar(AdocaoRequestDTO dto){

        validacoes.forEach(v -> v.validar(dto));

        Pet pet = petRepository.getReferenceById(dto.idPet());
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());

        Adocao adocao = new Adocao(tutor, pet, dto.motivo());
        adocaoRepository.save(adocao);

        return new AdocaoResponseDTO(adocao);

    }

    @Transactional
    public AdocaoResponseDTO aprovar(Long idAdocao){
        Adocao adocao = adocaoRepository.findById(idAdocao)
                        .orElseThrow(() -> new EntityNotFoundException("Erro ao aprovar: Adoção não encontrada no banco."));

        if(adocao.getStatus() != StatusAdocao.AGUARDANDO_AVALIACAO) {
            throw new AdocaoException("Erro: Adoção já foi processada anteriormente");
        }

        adocao.marcarComoAprovada();
        adocao.getPet().marcarComoAdotado();

        return new AdocaoResponseDTO(adocao);
    }

    @Transactional
    public AdocaoResponseDTO reprovar(ReprovarAdocaoDTO dto,Long idAdocao){
        Adocao adocao = adocaoRepository.findById(idAdocao)
                        .orElseThrow(() -> new EntityNotFoundException("Erro ao reprovar: Adoção não encontrada no banco."));

        if(adocao.getStatus() != StatusAdocao.AGUARDANDO_AVALIACAO){
            throw new AdocaoException("Erro: Adoção já foi processada anteriormente.");
        }

        adocao.marcarComoReprovada(dto.justificativa());

        return new AdocaoResponseDTO(adocao);
    }
}
