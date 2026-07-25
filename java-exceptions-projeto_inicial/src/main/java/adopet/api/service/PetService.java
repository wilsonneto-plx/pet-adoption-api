package adopet.api.service;

import adopet.api.dto.PetAtualizacaoDTO;
import adopet.api.dto.PetRequestDTO;
import adopet.api.dto.PetResponseDTO;
import adopet.api.exception.AdocaoException;
import adopet.api.model.Pet;
import adopet.api.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository repository;

    private final ImageStorageService imagemService;

    public List<PetResponseDTO> listarTodos(){
        return repository.findAll().stream().map(PetResponseDTO::new).toList();
    }

    public List<PetResponseDTO> listarPetsDisponiveis() {
        return repository.findAllByAdotadoFalse()
                .stream()
                .map(PetResponseDTO::new)
                .toList();
    }

    @Transactional
    public PetResponseDTO cadastrar (PetRequestDTO dto, MultipartFile imagem) {

        boolean nomeJaCadastrado = repository.existsByNome(dto.nome());
        if(nomeJaCadastrado) {
            throw new AdocaoException("Já existe um pet cadastrado com esse nome!");
        }

        String nomeImagem = imagemService.upload(imagem);

        Pet pet = dto.toEntity(nomeImagem);

        repository.save(pet);

        return new PetResponseDTO(pet);

    }

    @Transactional
    public PetResponseDTO atualizar (PetAtualizacaoDTO dados, Long id, MultipartFile novaImagem){

        Pet pet = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet não encontrado no sistema."));

        if (novaImagem != null && !novaImagem.isEmpty()) {

            String imagemAntiga = pet.getImagem();

            String caminhoNovaImagem = imagemService.upload(novaImagem);

            pet.atualizarImagem(caminhoNovaImagem);

            if (imagemAntiga != null && !imagemAntiga.isEmpty()) {
                imagemService.apagar(imagemAntiga);
            }
        }

        pet.atualizar(dados);

        return new PetResponseDTO(pet);

    }

    @Transactional
    public void excluir(Long id) {
        Pet pet = repository.findById(id)
                .orElseThrow( () -> new EntityNotFoundException("Pet não encontrado no sistema."));

        if (pet.getAdotado()) {
            throw new AdocaoException("Este pet não pode ser excluido pois já possui um histórico de adoção.");
        }

        String imagem = pet.getImagem();

        if (imagem != null && !imagem.isEmpty()) {
            imagemService.apagar(imagem);
        }

        repository.delete(pet);

    }
}
