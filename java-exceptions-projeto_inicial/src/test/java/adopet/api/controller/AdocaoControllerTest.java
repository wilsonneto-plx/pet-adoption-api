package adopet.api.controller;



import adopet.api.dto.AdocaoResponseDTO;
import adopet.api.service.AdocaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdocaoController.class)
public class AdocaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdocaoService service;

    @Test
    @DisplayName("Deve devolver código 200 (OK) e a lista de adoções ao buscar todas.")
    void deveDevolverCodigo200AoBuscarTodasAsAdocoes() throws Exception {

        AdocaoResponseDTO dto = mock(AdocaoResponseDTO.class);
        given(dto.idAdocao()).willReturn(1L);

        List<AdocaoResponseDTO> listaAdocoes = List.of(dto);

        given(service.listarTodos()).willReturn(listaAdocoes);

        mockMvc.perform(get("/adocoes")
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idAdocao").value(1L));

        then(service).should(times(1)).listarTodos();
    }

    @Test
    @DisplayName("Deve devolver código 200 (OK) e a adoção buscada pelo ID.")
    void deveDevolverCodigo200AoBuscarAdocaoPorId() throws Exception {

        Long idValido = 1L;
        AdocaoResponseDTO dto = mock(AdocaoResponseDTO.class);
        given(dto.idAdocao()).willReturn(idValido);

        given(service.listar(idValido)).willReturn(dto);

        mockMvc.perform(get("/adocoes/{id}", idValido)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idAdocao").value(idValido));

        then(service).should(times(1)).listar(idValido);
    }

    @Test
    @DisplayName("Deve devolver código 201 (Created), cabeçalho Location e o DTO ao solicitar adoção.")
    void deveDevolverCodigo201AoSolicitarAdocao() throws Exception {

        String jsonRequest = """
                {
                    "idPet": 1,
                    "idTutor": 1,
                    "motivo": "Tenho muito amor para dar e um quintal grande e seguro para o pet correr!"
                }
                """;

        AdocaoResponseDTO respostaDTO = mock(AdocaoResponseDTO.class);
        given(respostaDTO.idAdocao()).willReturn(10L);

        given(service.solicitar(any())).willReturn(respostaDTO);

        mockMvc.perform(post("/adocoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())

                .andExpect(header().exists("Location"))

                .andExpect(jsonPath("$.idAdocao").value(10L));

        then(service).should(times(1)).solicitar(any());
    }

    @Test
    @DisplayName("Deve devolver código 400 (Bad Request) ao tentar solicitar adoção com dados inválidos.")
    void deveDevolverCodigo400AoTentarSolicitarAdocaoComDadosInvalidos() throws Exception {

        String jsonInvalido = """
                {
                    "idPet": 1,
                    "idTutor": null,
                    "motivo": ""
                }
                """;

        mockMvc.perform(post("/adocoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest());

        then(service).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Deve devolver código 200 (OK) e a adoção aprovada ao disparar PUT na rota de aprovação.")
    void deveDevolverCodigo200AoAprovarAdocao() throws Exception {

        Long idValido = 1L;
        AdocaoResponseDTO dto = mock(AdocaoResponseDTO.class);
        given(dto.idAdocao()).willReturn(idValido);

        given(service.aprovar(idValido)).willReturn(dto);

        mockMvc.perform(put("/adocoes/{id}/aprovar", idValido)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.idAdocao").value(idValido));

        then(service).should(times(1)).aprovar(idValido);
    }

    @Test
    @DisplayName("Deve devolver código 404 (Not Found) ao tentar aprovar adoção com ID inexistente.")
    void deveDevolverCodigo404AoTentarAprovarAdocaoComIdInexistente() throws Exception {

        Long idInexistente = 999L;

        given(service.aprovar(idInexistente))
                .willThrow(new jakarta.persistence.EntityNotFoundException("Adoção não encontrada"));

        mockMvc.perform(put("/adocoes/{id}/aprovar", idInexistente)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());

        then(service).should(times(1)).aprovar(idInexistente);
    }

    @Test
    @DisplayName("Deve devolver código 200 (OK) e a adoção reprovada ao enviar PUT com justificativa.")
    void deveDevolverCodigo200AoReprovarAdocao() throws Exception {

        Long idValido = 1L;

        String jsonRequest = """
                {
                    "justificativa": "Infelizmente o ambiente não possui telas de proteção nas janelas para a segurança
                     do gato."
                }
                """;

        AdocaoResponseDTO respostaDTO = mock(AdocaoResponseDTO.class);
        given(respostaDTO.idAdocao()).willReturn(idValido);

        given(service.reprovar(any(), eq(idValido))).willReturn(respostaDTO);

        mockMvc.perform(put("/adocoes/{id}/reprovar", idValido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.idAdocao").value(idValido));

        then(service).should(times(1)).reprovar(any(), eq(idValido));
    }

    @Test
    @DisplayName("Deve devolver código 404 (Not Found) ao tentar reprovar adoção com ID inexistente.")
    void deveDevolverCodigo404AoTentarReprovarAdocaoComIdInexistente() throws Exception {

        Long idInexistente = 999L;

        String jsonRequest = """
                {
                    "justificativa": "Reprovação simulada para teste de ID inexistente."
                }
                """;

        given(service.reprovar(any(), eq(idInexistente)))
                .willThrow(new jakarta.persistence.EntityNotFoundException("Adoção não encontrada"));


        mockMvc.perform(put("/adocoes/{id}/reprovar", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());

        then(service).should(times(1)).reprovar(any(), eq(idInexistente));
    }

}
