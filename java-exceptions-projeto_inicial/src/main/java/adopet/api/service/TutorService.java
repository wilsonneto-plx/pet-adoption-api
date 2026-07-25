package adopet.api.service;

import adopet.api.dto.TutorAtualizaoDTO;
import adopet.api.dto.TutorRequestDTO;
import adopet.api.dto.TutorResponseDTO;
import adopet.api.exception.AdocaoException;
import adopet.api.model.Pet;
import adopet.api.model.Tutor;
import adopet.api.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorService {

    private final TutorRepository repository;

    public List<TutorResponseDTO> listarTodos(){
        return repository.findAll().stream().map(TutorResponseDTO::new).toList();
    }

    @Transactional
    public TutorResponseDTO cadastrar(TutorRequestDTO dados){

        boolean emailJaCadastrado = repository.existsByEmail(dados.email());
        if(emailJaCadastrado) {
            throw new AdocaoException("E-mail já cadastrado no banco.");
        }

        Tutor tutor = dados.toEntity();

        repository.save(tutor);

        return new TutorResponseDTO(tutor);

    }

    @Transactional
    public TutorResponseDTO atualizar(TutorAtualizaoDTO dados, Long id){

        Tutor tutor = repository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Tutor não encontrado no sistema."));

        tutor.atualizarDados(dados);

        return new TutorResponseDTO(tutor);

    }

    @Transactional
    public void excluir(Long id) {

        Tutor tutor = repository.findById(id)
                .orElseThrow( () -> new EntityNotFoundException("Tutor não encontrado no sistema."));

        if (!tutor.getAdocoes().isEmpty()) {
            throw new AdocaoException("Este tutor não pode ser excluido pois já possui um histórico de adoção.");
        }

        repository.delete(tutor);
    }
}
