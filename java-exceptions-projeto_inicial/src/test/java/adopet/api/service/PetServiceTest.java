package adopet.api.service;

import adopet.api.dto.PetAtualizacaoDTO;
import adopet.api.dto.PetRequestDTO;
import adopet.api.dto.PetResponseDTO;
import adopet.api.exception.AdocaoException;
import adopet.api.model.Pet;
import adopet.api.model.TipoPet;
import adopet.api.model.TipoPorte;
import adopet.api.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository repository;

    @Mock
    private ImageStorageService imageService;

    @InjectMocks
    private PetService service;

    @Test
    @DisplayName("Deve retornar uma lista de DTOs ao listar todos os pets.")
    void deveListarTodosOsPets() {

        Pet pet1 = criarPetPadrao();
        Pet pet2 = criarPetPadrao();

        given(repository.findAll()).willReturn(List.of(pet1,pet2));

        List<PetResponseDTO> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        then(repository).should(times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar somente os pets disponíveis.")
    void deveListarSomenteOsPetsDisponiveis() {

        Pet petDisponivel = criarPetPadrao();

        given(repository.findAllByAdotadoFalse()).willReturn(List.of(petDisponivel));

        List<PetResponseDTO> resultado = service.listarPetsDisponiveis();

        assertEquals(1, resultado.size());

        then(repository).should(times(1)).findAllByAdotadoFalse();

        then(repository).should(never()).findAll();

    }

    @Test
    @DisplayName("Deve cadastrar um Pet com sucesso.")
    void deveCadastrarUmPetComSucesso() {

        PetRequestDTO dto = mock(PetRequestDTO.class);
        MultipartFile imageMock = mock(MultipartFile.class);
        Pet petFake = criarPetPadrao();

        given(dto.nome()).willReturn("Rex");
        given(repository.existsByNome("Rex")).willReturn(false);

        String nomeImagem = "foto.png";

        given(imageService.upload(imageMock)).willReturn(nomeImagem);

        given(dto.toEntity(nomeImagem)).willReturn(petFake);

        PetResponseDTO resultado = service.cadastrar(dto, imageMock);

        assertNotNull(resultado);
        assertEquals("Rex", resultado.nome());
        assertEquals(nomeImagem, resultado.imagem());

        then(repository).should(times(1)).save(petFake);
    }

    @Test
    @DisplayName("Deve lançar AdocaoException ao tentar cadastrar pet duplicado")
    void deveLancarExcecaoAoCadastrarPetComNomeJaExistente() {

        PetRequestDTO dto = mock(PetRequestDTO.class);
        MultipartFile imagemMock = mock(MultipartFile.class);

        given(dto.nome()).willReturn("Rex");

        given(repository.existsByNome("Rex")).willReturn(true);

        assertThrows(AdocaoException.class, () -> {
            service.cadastrar(dto, imagemMock);
        });

        then(imageService).should(never()).upload(any());
        then(repository).should(never()).save((any()));

    }

    @Test
    @DisplayName("Deve atualizar os dados, fazer upload da nova imagem e apagar a imagem antiga.")
    void deveAtualizarUmPetComSucesso(){

        PetAtualizacaoDTO dto = mock(PetAtualizacaoDTO.class);
        MultipartFile novaImagem = mock(MultipartFile.class);
        Long idPet = 1L;

        Pet pet = criarPetPadrao();

        given(dto.nome()).willReturn("Max");

        given(repository.findById(idPet)).willReturn(Optional.of(pet));

        given(novaImagem.isEmpty()).willReturn(false);

        String imagemAntiga = pet.getImagem();
        String caminhoNovaImagem = "caminho/foto_nova.png";

        given(imageService.upload(novaImagem)).willReturn(caminhoNovaImagem);

        PetResponseDTO resultado = service.atualizar(dto, idPet, novaImagem);

        assertNotNull(resultado);
        assertEquals(caminhoNovaImagem, resultado.imagem());
        assertEquals("Max", resultado.nome());

        then(imageService).should(times(1)).upload(novaImagem);

        then(imageService).should(times(1)).apagar(imagemAntiga);

    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar atualizar um pet que não existe.")
    void deveLancarExcecaoAoAtualizarUmPetInexistente() {

        PetAtualizacaoDTO dto = mock(PetAtualizacaoDTO.class);
        Long idInvalido = 99L;
        MultipartFile imagemMock = mock(MultipartFile.class);

        given(repository.findById(idInvalido)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.atualizar(dto, idInvalido, imagemMock);
        } );

        then(imageService).should(never()).upload(any());

    }

    @Test
    @DisplayName("Deve atualizar um pet sem uma foto nova e não apagar a foto antiga.")
    void deveAtualizarPetSemEnviarNovaImagemENaoApagarAAntiga() {

        PetAtualizacaoDTO dto = mock(PetAtualizacaoDTO.class);
        Pet pet = criarPetPadrao();
        Long idValido = 1L;

        String imagemAntiga = pet.getImagem();

        given(repository.findById(idValido)).willReturn(Optional.of(pet));

        PetResponseDTO resultado = service.atualizar(dto, idValido, null);

        assertNotNull(resultado);
        assertEquals(imagemAntiga, resultado.imagem());

        then(imageService).should(never()).upload(any());
        then(imageService).should(never()).apagar(any());

    }

    @Test
    @DisplayName("Deve excluir um pet com sucesso e apagar sua foto.")
    void deveExcluirPetComSucesso() {

        Long idValido = 1L;
        Pet pet = criarPetPadrao();

        given(repository.findById(idValido)).willReturn(Optional.of(pet));

        String imagemPet = pet.getImagem();

        service.excluir(idValido);

        then(imageService).should(times(1)).apagar(imagemPet);
        then(repository).should(times(1)).delete(pet);

    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar excluir um pet que não existe.")
    void deveLancarExcecaoAoTentarExcluirPetInexistente() {

        Long idInvalido = 99L;
        Pet pet = criarPetPadrao();

        given(repository.findById(idInvalido)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.excluir(idInvalido);
        });

        then(imageService).should(never()).apagar(any());
        then(repository).should(never()).delete(any());

    }

    @Test
    @DisplayName("Deve lançar AdocaoException ao tentar excluir um pet com histórico de adoção.")
    void deveLancarExcecaoAoTentarExcluirPetComHistoricoDeAdocao() {

    }

    private Pet criarPetPadrao() {
        return new Pet("Rex", 3, TipoPet.CACHORRO, TipoPorte.MEDIO, "foto.png");
    }
}
