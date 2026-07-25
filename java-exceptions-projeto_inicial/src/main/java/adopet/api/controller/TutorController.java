package adopet.api.controller;


import adopet.api.dto.TutorAtualizaoDTO;
import adopet.api.dto.TutorRequestDTO;
import adopet.api.dto.TutorResponseDTO;
import adopet.api.service.TutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tutores")
public class TutorController {

    private final TutorService service;

    @GetMapping
    public ResponseEntity<List<TutorResponseDTO>> buscarTodos(){
        List<TutorResponseDTO> tutores = service.listarTodos();
        return ResponseEntity.ok(tutores);
    }

    @PostMapping
    public ResponseEntity<TutorResponseDTO> cadastrar(@RequestBody @Valid TutorRequestDTO dados,
                                                      UriComponentsBuilder uriBuilder){

        TutorResponseDTO tutor = this.service.cadastrar(dados);

        URI uri = uriBuilder.path("/tutores/{id}").buildAndExpand(tutor.id()).toUri();

        return ResponseEntity.created(uri).body(tutor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid TutorAtualizaoDTO dados) {

        TutorResponseDTO tutorAtualizado =  service.atualizar(dados, id);
        return ResponseEntity.ok(tutorAtualizado);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        service.excluir(id);
        return ResponseEntity.noContent().build();

    }

}
