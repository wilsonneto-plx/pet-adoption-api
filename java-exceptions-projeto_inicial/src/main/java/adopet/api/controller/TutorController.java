package adopet.api.controller;


import adopet.api.controller.doc.TutorControllerOpenAPI;
import adopet.api.dto.TutorAtualizacaoDTO;
import adopet.api.dto.TutorRequestDTO;
import adopet.api.dto.TutorResponseDTO;
import adopet.api.service.TutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tutores")
public class TutorController implements TutorControllerOpenAPI {

    private final TutorService service;

    @Override
    @GetMapping
    public ResponseEntity<List<TutorResponseDTO>> buscarTodos(){
        List<TutorResponseDTO> tutores = service.listarTodos();
        return ResponseEntity.ok(tutores);
    }

    @Override
    @PostMapping
    public ResponseEntity<TutorResponseDTO> cadastrar(@RequestBody @Valid TutorRequestDTO dados,
                                                      UriComponentsBuilder uriBuilder){

        TutorResponseDTO tutor = this.service.cadastrar(dados);

        URI uri = uriBuilder.path("/tutores/{id}").buildAndExpand(tutor.id()).toUri();

        return ResponseEntity.created(uri).body(tutor);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TutorResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid TutorAtualizacaoDTO dados) {

        TutorResponseDTO tutorAtualizado =  service.atualizar(dados, id);
        return ResponseEntity.ok(tutorAtualizado);

    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        service.excluir(id);
        return ResponseEntity.noContent().build();

    }
}
