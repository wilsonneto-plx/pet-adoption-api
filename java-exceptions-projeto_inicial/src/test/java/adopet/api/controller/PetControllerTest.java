package adopet.api.controller;

import adopet.api.dto.PetResponseDTO;
import adopet.api.service.PetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetService service;

    @Test
    @DisplayName("Deve devolver código 200 (OK) e a lista de pets ao buscar todos.")
    void deveDevolverCodigo200AoBuscarTodosOsPets() throws Exception {

        PetResponseDTO dto = mock(PetResponseDTO.class);
        given(dto.id()).willReturn(1L);
        given(dto.nome()).willReturn("Rex");

        List<PetResponseDTO> listaPets = List.of(dto);

        given(service.listarTodos()).willReturn(listaPets);

        mockMvc.perform(get("/pets")
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Rex"));

        then(service).should(times(1)).listarTodos();
    }

    @Test
    @DisplayName("Deve devolver código 200 (OK) e a lista somente com os pets disponíveis.")
    void deveDevolverCodigo200AoBuscarPetsDisponiveis() throws Exception {

        PetResponseDTO dto = mock(PetResponseDTO.class);
        given(dto.id()).willReturn(1L);
        given(dto.nome()).willReturn("Rex");

        List<PetResponseDTO> listaPets = List.of(dto);

        given(service.listarPetsDisponiveis()).willReturn(listaPets);

        mockMvc.perform(get("/pets/disponiveis")
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Rex"));

        then(service).should(times(1)).listarPetsDisponiveis();
    }

    @Test
    @DisplayName("Deve devolver código 204 (No Content) ao excluir um pet com sucesso.")
    void deveDevolverCodigo204AoExcluirPet() throws Exception {

        Long idValido = 1L;

        mockMvc.perform(delete("/pets/{id}", idValido))

                .andExpect(status().isNoContent());

        then(service).should(times(1)).excluir(idValido);
    }

    @Test
    @DisplayName("Deve devolver código 201 (Created), cabeçalho Location e o DTO ao cadastrar pet.")
    void deveDevolverCodigo201AoCadastrarPet() throws Exception {

        MockMultipartFile imagem = new MockMultipartFile(
                "imagem",
                "foto.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "foto-falsa".getBytes()
        );

        String jsonDados = """
        {
            "nome": "Rex",
            "idade": 2,
            "tipo": "CACHORRO",
            "porte": "MEDIO"
        }
        """;

        MockMultipartFile dados = new MockMultipartFile(
                "dados",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                jsonDados.getBytes()
        );

        PetResponseDTO respostaDTO = mock(PetResponseDTO.class);
        given(respostaDTO.id()).willReturn(1L);
        given(respostaDTO.nome()).willReturn("Rex");

        given(service.cadastrar(any(), any())).willReturn(respostaDTO);

        mockMvc.perform(multipart("/pets")
                        .file(imagem)
                        .file(dados)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())

                .andExpect(header().exists("Location"))

                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Rex"));

        then(service).should(times(1)).cadastrar(any(), any());
    }

    @Test
    @DisplayName("Deve devolver código 400 (Bad Request) ao tentar cadastrar pet com dados inválidos.")
    void deveDevolverCodigo400AoTentarCadastrarPetComDadosInvalidos() throws Exception {

        MockMultipartFile imagem = new MockMultipartFile(
                "imagem",
                "foto.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "foto-falsa".getBytes()
        );

        String jsonInvalido = """
                {
                    "nome": "",
                    "idade": null,
                    "tipo": "CACHORRO",
                    "porte": "MEDIO"
                }
                """;

        MockMultipartFile dados = new MockMultipartFile(
                "dados",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                jsonInvalido.getBytes()
        );

        mockMvc.perform(multipart("/pets")
                        .file(imagem)
                        .file(dados)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest());

        then(service).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Deve devolver código 200 (OK) e o pet atualizado ao fazer PUT na rota.")
    void deveDevolverCodigo200AoAtualizarPet() throws Exception {

        Long idValido = 1L;

        String jsonDados = """
                {
                    "nome": "Rex Atualizado",
                    "porte": "GRANDE",
                    "idade": 3
                }
                """;

        MockMultipartFile dados = new MockMultipartFile(
                "dados",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                jsonDados.getBytes()
        );

        PetResponseDTO respostaDTO = mock(PetResponseDTO.class);
        given(respostaDTO.id()).willReturn(idValido);
        given(respostaDTO.nome()).willReturn("Rex Atualizado");

        given(service.atualizar(any(), any(), any())).willReturn(respostaDTO);

        mockMvc.perform(multipart("/pets/{id}", idValido)
                        .file(dados)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Rex Atualizado"));

        then(service).should(times(1)).atualizar(any(), any(), any());
    }





}
