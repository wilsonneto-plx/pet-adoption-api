package adopet.api.controller;

import adopet.api.dto.TutorResponseDTO;
import adopet.api.service.TutorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
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

@WebMvcTest(TutorController.class)
public class TutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TutorService service;

    @Test
    @DisplayName("Deve devolver código 200 (OK) e a lista de tutores ao buscar todos.")
    void deveDevolverCodigo200AoBuscarTodosOsTutores() throws Exception {

        TutorResponseDTO dto = mock(TutorResponseDTO.class);
        given(dto.id()).willReturn(1L);

        given(service.listarTodos()).willReturn(List.of(dto));

        mockMvc.perform(get("/tutores")
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L));

        then(service).should(times(1)).listarTodos();
    }

    @Test
    @DisplayName("Deve devolver código 204 (No Content) ao excluir um tutor com sucesso.")
    void deveDevolverCodigo204AoExcluirTutor() throws Exception {

        Long idValido = 1L;

        mockMvc.perform(delete("/tutores/{id}", idValido))
                .andExpect(status().isNoContent());

        then(service).should(times(1)).excluir(idValido);
    }

    @Test
    @DisplayName("Deve devolver código 201 (Created), cabeçalho Location e o DTO ao cadastrar tutor.")
    void deveDevolverCodigo201AoCadastrarTutor() throws Exception {

        String jsonRequest = """
                {
                    "nome": "Ana Silva",
                    "email": "ana.silva@gmail.com",
                    "telefone": "1187654321"
                }
                """;

        TutorResponseDTO respostaDTO = mock(TutorResponseDTO.class);
        given(respostaDTO.id()).willReturn(1L);

        given(service.cadastrar(any())).willReturn(respostaDTO);

        mockMvc.perform(post("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L));

        then(service).should(times(1)).cadastrar(any());
    }

    @Test
    @DisplayName("Deve devolver código 400 (Bad Request) ao tentar cadastrar tutor com dados inválidos.")
    void deveDevolverCodigo400AoTentarCadastrarTutorComDadosInvalidos() throws Exception {

        String jsonInvalido = """
                {
                    "nome": "",
                    "email": "ana.silva-sem-arroba.com",
                    "telefone": "123"
                }
                """;

        mockMvc.perform(post("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest());

        then(service).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Deve devolver código 200 (OK) e o tutor atualizado ao disparar PUT.")
    void deveDevolverCodigo200AoAtualizarTutor() throws Exception {

        Long idValido = 1L;

        String jsonRequest = """
                {
                    "nome": "Ana Silva Atualizada",
                    "telefone": "(11) 98765-4321"
                }
                """;

        TutorResponseDTO respostaDTO = mock(TutorResponseDTO.class);
        given(respostaDTO.id()).willReturn(idValido);

        given(service.atualizar(any(), eq(idValido))).willReturn(respostaDTO);

        mockMvc.perform(put("/tutores/{id}", idValido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(idValido));

        then(service).should(times(1)).atualizar(any(), eq(idValido));
    }

    @Test
    @DisplayName("Deve devolver código 404 (Not Found) ao tentar atualizar tutor com ID inexistente.")
    void deveDevolverCodigo404AoTentarAtualizarTutorComIdInexistente() throws Exception {

        Long idInexistente = 999L;

        String jsonRequest = """
                {
                    "nome": "Tutor Fantasma",
                    "telefone": "(11) 98765-4321"
                }
                """;

        given(service.atualizar(any(), eq(idInexistente)))
                .willThrow(new jakarta.persistence.EntityNotFoundException("Tutor não encontrado"));

        mockMvc.perform(put("/tutores/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());

        then(service).should(times(1)).atualizar(any(), eq(idInexistente));
    }

}
