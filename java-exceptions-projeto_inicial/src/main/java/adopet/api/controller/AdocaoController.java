package adopet.api.controller;

import adopet.api.dto.AdocaoResponseDTO;
import adopet.api.dto.ReprovarAdocaoDTO;
import adopet.api.dto.AdocaoRequestDTO;
import adopet.api.service.AdocaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adocoes")
public class AdocaoController {

    private final AdocaoService service;

    @GetMapping
    public ResponseEntity<List<AdocaoResponseDTO>> buscarTodos(){
        List<AdocaoResponseDTO> adocoes = service.listarTodos();
        return ResponseEntity.ok(adocoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdocaoResponseDTO> buscar(@PathVariable Long id){
        AdocaoResponseDTO adocao = service.listar(id);
        return ResponseEntity.ok(adocao);
    }

    @PostMapping
    public ResponseEntity<AdocaoResponseDTO> solicitar(@RequestBody @Valid AdocaoRequestDTO dados,
                                                       UriComponentsBuilder uriBuilder){
        AdocaoResponseDTO adocao = this.service.solicitar(dados);

        URI uri = uriBuilder.path("/adocoes/{id}").buildAndExpand(adocao.idAdocao()).toUri();

        return ResponseEntity.created(uri).body(adocao);
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<AdocaoResponseDTO> aprovar(@PathVariable Long id){
        AdocaoResponseDTO adocao = this.service.aprovar(id);
        return ResponseEntity.ok(adocao);
    }

    @PutMapping("/{id}/reprovar")
    public ResponseEntity<AdocaoResponseDTO> reprovar(@PathVariable Long id, @RequestBody @Valid ReprovarAdocaoDTO dto){
        AdocaoResponseDTO adocao = this.service.reprovar(dto, id);
        return ResponseEntity.ok(adocao);
    }
}
