package adopet.api.service;

import adopet.api.dto.AdocaoRequestDTO;
import adopet.api.dto.AdocaoResponseDTO;
import adopet.api.dto.ReprovarAdocaoDTO;
import adopet.api.exception.AdocaoException;
import adopet.api.model.*;
import adopet.api.repository.AdocaoRepository;
import adopet.api.repository.PetRepository;
import adopet.api.repository.TutorRepository;
import adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AdocaoServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private ValidacaoSolicitacaoAdocao validador;

    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    @InjectMocks
    private AdocaoService service;


    @Test
    @DisplayName("Deve listar todas as adoções.")
    void deveListarTodasAsAdocoes(){

        Adocao adocao1 = criarAdocaoPadrao();
        Adocao adocao2 = criarAdocaoPadrao();

        given(adocaoRepository.findAll()).willReturn(List.of(adocao1,adocao2));

        List<AdocaoResponseDTO> resultado = service.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        then(adocaoRepository).should(times(1)).findAll();

    }

    @Test
    @DisplayName("Deve retornar uma adoção através do id.")
    void deveRetornarUmaAdocaoPeloId() {

        Long idValido = 1L;
        Adocao adocao = criarAdocaoPadrao();

        given(adocaoRepository.findById(idValido)).willReturn(Optional.of(adocao));

        AdocaoResponseDTO resultado = service.listar(idValido);

        assertNotNull(resultado);

        then(adocaoRepository).should(times(1)).findById(idValido);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao buscar uma adoção que não existe.")
    void deveLancarExcecaoAoBuscarAdocaoInexistente() {

        Long idInvalido = 99L;

        given(adocaoRepository.findById(idInvalido)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.listar(idInvalido);
        });
    }

    @Test
    @DisplayName("Deve solicitar uma adoção com sucesso.")
    void deveSolicitarAdocaoComSucesso() {

        validacoes.add(validador);

        Pet pet = criarAdocaoPadrao().getPet();
        Tutor tutor = criarAdocaoPadrao().getTutor();

        AdocaoRequestDTO dto = mock(AdocaoRequestDTO.class);
        given(dto.idPet()).willReturn(pet.getId());
        given(dto.idTutor()).willReturn(tutor.getId());
        given(dto.motivo()).willReturn("Quero um companheiro para corridas no parque.");

        given(petRepository.getReferenceById(pet.getId())).willReturn(pet);
        given(tutorRepository.getReferenceById(tutor.getId())).willReturn(tutor);

        AdocaoResponseDTO resultado = service.solicitar(dto);

        assertNotNull(resultado);
        assertEquals(dto.motivo(), resultado.motivo());
        assertEquals(dto.idTutor(), resultado.idTutor());
        assertEquals(dto.idPet(), resultado.idPet());

        then(validador).should(times(1)).validar(dto);
        then(petRepository).should(times(1)).getReferenceById(pet.getId());
        then(tutorRepository).should(times(1)).getReferenceById(tutor.getId());

        then(adocaoRepository).should(times(1)).save(any(Adocao.class));

    }

    @Test
    @DisplayName("Deve lançar AdocaoException quando alguma validação da solicitação der erro.")
    void deveLancarExcecaoQuandoAValidacaoDerErro() {

        validacoes.add(validador);

        AdocaoRequestDTO dto = mock(AdocaoRequestDTO.class);

        willThrow(new AdocaoException("Erro de validação!")).given(validador).validar(dto);

        assertThrows(AdocaoException.class, () -> {
            service.solicitar(dto);
        });

        then(adocaoRepository).should(never()).save(any(Adocao.class));

        then(petRepository).should(never()).getReferenceById(any());
        then(tutorRepository).should(never()).getReferenceById(any());

    }

    @Test
    @DisplayName("Deve aprovar uma adoção com sucesso.")
    void deveAprovarAdocaoComSucesso() {

        Long idValido = 1L;
        Adocao adocao = criarAdocaoPadrao();

        given(adocaoRepository.findById(idValido)).willReturn(Optional.of(adocao));

        AdocaoResponseDTO resultado = service.aprovar(idValido);

        assertNotNull(resultado);
        assertEquals(adocao.getPet().getId(), resultado.idPet());
        assertEquals(adocao.getTutor().getId(), resultado.idTutor());
        assertEquals(StatusAdocao.APROVADO, resultado.status());

        then(adocaoRepository).should(times(1)).findById(idValido);

    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar aprovar uma adoção que não existe.")
    void deveLancarExcecaoAoAprovarAdocaoInexistente() {

        Long idInvalido = 99L;

        given(adocaoRepository.findById(idInvalido)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.aprovar(idInvalido);
        });

        then(adocaoRepository).should(times(1)).findById(idInvalido);

    }

    @Test
    @DisplayName("Deve lançar AdocaoException ao tentar aprovar uma adoção que já foi processada anteriormente.")
    void deveLancarExcecaoAoTentarAprovarAdocaoJaProcessada() {

        Long idValido = 1L;
        Adocao adocao = criarAdocaoPadrao();
        adocao.marcarComoAprovada();

        given(adocaoRepository.findById(idValido)).willReturn(Optional.of(adocao));

        assertThrows(AdocaoException.class, () -> {
           service.aprovar(idValido);
        });

        then(adocaoRepository).should(times(1)).findById(idValido);

    }

    @Test
    @DisplayName("Deve reprovar uma adoção com sucesso.")
    void deveReprovarAdocaoComSucesso(){

        Long idValido =  1L;
        Adocao adocao = criarAdocaoPadrao();
        ReprovarAdocaoDTO dto = mock(ReprovarAdocaoDTO.class);

        given(dto.justificativa()).willReturn("Menor de 18 anos");

        given(adocaoRepository.findById(idValido)).willReturn(Optional.of(adocao));

        AdocaoResponseDTO resultado = service.reprovar(dto, idValido);

        assertNotNull(resultado);
        assertEquals(dto.justificativa(), resultado.justificativa());
        assertEquals(StatusAdocao.REPROVADO, resultado.status());

        then(adocaoRepository).should(times(1)).findById(idValido);

    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar reprovar uma adoção que não existe. ")
    void deveLancarExcecaoAoTentarReprovarAdocaoInexistente(){

        Long idInvalido = 99L;
        ReprovarAdocaoDTO dto = mock(ReprovarAdocaoDTO.class);

        given(adocaoRepository.findById(idInvalido)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.reprovar(dto ,idInvalido);
        });

        then(adocaoRepository).should(times(1)).findById(idInvalido);

    }

    @Test
    @DisplayName("Deve lançar AdocaoException ao tentar reprovar uma adoção que já foi processada anteriormente.")
    void deveLancarExcecaoAoTentarReprovarAdocaoJaProcessada() {

        Long idValido = 1L;
        Adocao adocao = criarAdocaoPadrao();
        adocao.marcarComoReprovada("Menor de 18 anos");
        ReprovarAdocaoDTO dto = mock(ReprovarAdocaoDTO.class);

        given(adocaoRepository.findById(idValido)).willReturn(Optional.of(adocao));

        assertThrows(AdocaoException.class, () -> {
            service.reprovar(dto, idValido);
        });

        then(adocaoRepository).should(times(1)).findById(idValido);

    }

    private Adocao criarAdocaoPadrao() {

        Pet pet = new Pet("Rex", 3, TipoPet.CACHORRO, TipoPorte.MEDIO, "foto.png");
        Tutor tutor = new Tutor("João da Silva", "joao@email.com", "(11) 99999-9999");

        return new Adocao(tutor, pet, "Quero um companheiro para corridas no parque.");
    }
}
