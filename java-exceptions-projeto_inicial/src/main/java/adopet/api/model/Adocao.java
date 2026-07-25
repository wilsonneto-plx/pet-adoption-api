package adopet.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "adocoes")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Adocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Tutor tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Pet pet;

    @Column(nullable = false)
    @NotBlank
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAdocao status;

    private String justificativa;

    @Column(nullable = false)
    private LocalDateTime dataSolicitacao;

    public Adocao(Tutor tutor, Pet pet, String motivo){
        this.tutor = tutor;
        this.pet = pet;
        this.motivo = motivo;
        this.status = StatusAdocao.AGUARDANDO_AVALIACAO;
        this.dataSolicitacao = LocalDateTime.now();
    }

    public void marcarComoAprovada(){

        this.status = StatusAdocao.APROVADO;
    }

    public void marcarComoReprovada(String justificativa)
    {
        this.status = StatusAdocao.REPROVADO;
        this.justificativa = justificativa;
    }
}
