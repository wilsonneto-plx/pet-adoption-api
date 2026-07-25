package adopet.api.controller;

import adopet.api.dto.PetAtualizacaoDTO;
import adopet.api.dto.PetRequestDTO;
import adopet.api.dto.PetResponseDTO;
import adopet.api.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pets")
public class PetController {

    private final PetService service;

    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> buscarTodos(){
        List<PetResponseDTO> pets = service.listarTodos();
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<PetResponseDTO>> buscarPetsDisponiveis(){
        List<PetResponseDTO> petsDisponiveis = service.listarPetsDisponiveis();
        return ResponseEntity.ok(petsDisponiveis);
    }

    @PostMapping
    public ResponseEntity<PetResponseDTO> cadastrar(@RequestPart("dados") @Valid PetRequestDTO dados,
                                            @RequestPart("imagem") MultipartFile imagem,
                                                    UriComponentsBuilder uriBuilder){

        PetResponseDTO pet = service.cadastrar(dados, imagem);

        URI uri = uriBuilder.path("/pets/{id}").buildAndExpand(pet.id()).toUri();

        return ResponseEntity.created(uri).body(pet);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestPart("dados") @Valid  PetAtualizacaoDTO dados,
            @RequestPart(value = "imagem", required = false) MultipartFile novaImagem){

        PetResponseDTO petAtualizado = service.atualizar(dados, id, novaImagem);

        return ResponseEntity.ok(petAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        service.excluir(id);
        return ResponseEntity.noContent().build();

    }

}
