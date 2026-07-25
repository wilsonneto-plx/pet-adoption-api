package adopet.api.repository;

import adopet.api.model.Pet;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    boolean existsByNome(String nome);

    List<Pet> findAllByAdotadoFalse();
}
