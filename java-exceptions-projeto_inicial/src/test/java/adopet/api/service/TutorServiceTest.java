package adopet.api.service;


import adopet.api.dto.TutorAtualizaoDTO;
import adopet.api.dto.TutorRequestDTO;
import adopet.api.dto.TutorResponseDTO;
import adopet.api.exception.AdocaoException;
import adopet.api.model.Adocao;
import adopet.api.model.Tutor;
import adopet.api.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TutorServiceTest {

    @Mock
    private TutorRepository repository;

    @InjectMocks
    private TutorService service;

    @Test
    @DisplayName("Deve listar todos os tutores.")
    void deveListarTodosOsTutoresComSucesso() {

        Tutor tutor1 = criarTutorPadrao();
        Tutor tutor2 = criarTutorPadrao();

        given(repository.findAll()).willReturn(List.of(tutor1, tutor2));

        List<TutorResponseDTO> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        then(repository).should(times(1)).findAll();

    }

    @Test
    @DisplayName("Deve cadastrar um tutor com sucesso.")
    void deveCadastrarTutorComSucesso() {

        Tutor tutor = criarTutorPadrao();
        TutorRequestDTO dto = mock(TutorRequestDTO.class);

        given(dto.email()).willReturn(tutor.getEmail());
        given(dto.toEntity()).willReturn(tutor);

        given(repository.existsByEmail(tutor.getEmail())).willReturn(false);

        TutorResponseDTO resultado = service.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals(tutor.getNome(), resultado.nome());

        then(repository).should(times(1)).save(tutor);

    }

    @Test
    @DisplayName("Deve lançar AdocaoException ao cadastrar um tutor com email já existente.")
    void deveLancarExcecaoAoCadastrarTutorComEmailExistente(){

        TutorRequestDTO dto = mock(TutorRequestDTO.class);
        Tutor tutor = criarTutorPadrao();

        given(dto.email()).willReturn(tutor.getEmail());
        given(repository.existsByEmail(dto.email())).willReturn(true);

        assertThrows(AdocaoException.class, () -> {
            service.cadastrar(dto);
        });

        then(repository).should(never()).save(any());

    }

    @Test
    @DisplayName("Deve atualizar um tutor existente com sucesso.")
    void deveAtualizarTutorComSucesso() {

        Tutor tutor = criarTutorPadrao();
        Long idValido = 1L;
        TutorAtualizaoDTO dto = mock(TutorAtualizaoDTO.class);

        given(dto.nome()).willReturn("Wilson Neto");
        given(dto.email()).willReturn("wilsonneto10@gmail.com");
        given(dto.telefone()).willReturn("(86) 99937-9725");

        given(repository.findById(idValido)).willReturn(Optional.of(tutor));

        TutorResponseDTO resultado = service.atualizar(dto, idValido);

        assertNotNull(resultado);
        assertEquals("Wilson Neto", resultado.nome());
        assertEquals("wilsonneto10@gmail.com", resultado.email());
        assertEquals("(86) 99937-9725", resultado.telefone());

    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar atualizar um tutor que não existe.")
    void deveLancarExcecaoAoAtualizarTutorInexistente() {

        Long idInvalido = 99L;
        TutorAtualizaoDTO dto = mock(TutorAtualizaoDTO.class);

        given(repository.findById(idInvalido)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.atualizar(dto, idInvalido);
        });
    }

    @Test
    @DisplayName("Deve excluir um tutor com sucesso.")
    void deveExcluirTutorComSucesso() {

        Long idValido = 1L;
        Tutor tutor = criarTutorPadrao();

        given(repository.findById(idValido)).willReturn(Optional.of(tutor));

        service.excluir(idValido);

        then(repository).should(times(1)).delete(any());

    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar excluir um tutor que não existe.")
    void deveLancarExcecaoAoTentarExcluirTutorInexistente() {

        Long idInvalido = 99L;

        given(repository.findById(idInvalido)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.excluir(idInvalido);
        });

        then(repository).should(never()).delete(any());

    }

    @Test
    @DisplayName("Deve lançar AdocaoException ao tentar excluir um tutor com histórico de adoção.")
    void deveLancarExcecaoAoTentarExcluirTutorComHistoricoDeAdocao() {

        Long idValido = 1L;
        Tutor tutor = criarTutorPadrao();
        tutor.getAdocoes().add(mock(Adocao.class));

        given(repository.findById(idValido)).willReturn(Optional.of(tutor));

        assertThrows(AdocaoException.class, () -> {
            service.excluir(idValido);
        });

        then(repository).should(never()).delete(any());
    }



    private Tutor criarTutorPadrao() {
        return new Tutor("Wil", "wilsonnetoplx@gmail.com","86999378745");
    }
}
