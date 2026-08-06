package adopet.api.model;


import adopet.api.dto.PetAtualizacaoDTO;
import jakarta.persistence.*;;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "pets")
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer idade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPet tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPorte porte;

    @Column(nullable = false)
    private Boolean adotado;

    @Column(nullable = false)
    private String imagem;

    @OneToMany(mappedBy = "pet")
    private List<Adocao> adocoes = new ArrayList<>();

    public Pet(String nome, Integer idade, TipoPet tipo, TipoPorte porte,String imagem )
    {
        this.nome = nome;
        this.idade = idade;
        this.tipo = tipo;
        this.porte = porte;
        this.imagem = imagem;
        this.adotado = false;
    }

    public void marcarComoAdotado(){
        this.adotado = true;
    }

    public void marcarComoDisponivel() {
        this.adotado = false;
    }

    public void atualizar(PetAtualizacaoDTO dados){
        if (dados.nome() != null) {this.nome = dados.nome();}
        if (dados.idade() != null) {this.idade = dados.idade();}
        if (dados.porte() != null) {this.porte = dados.porte();}
    }

    public void atualizarImagem(String caminhoNovaImagem) {
        if (caminhoNovaImagem != null) {this.imagem = caminhoNovaImagem;}
    }
}
