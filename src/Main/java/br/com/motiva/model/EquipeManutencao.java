package br.com.motiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "EQUIPE_MANUTENCAO")
public class EquipeManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "nome da equipe é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "especialidade é obrigatória")
    @Column(nullable = false, length = 100)
    private String especialidade;

    @Column(nullable = false, length = 10)
    private String direcao;

    public EquipeManutencao() {
    }

    public EquipeManutencao(String nome, String especialidade, String direcao) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.direcao = normalizarDirecao(direcao);
    }

    public EquipeManutencao(Integer id, String nome, String especialidade, String direcao) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.direcao = normalizarDirecao(direcao);
    }

    private String normalizarDirecao(String valor) {
        if (valor == null) {
            return "NORTE";
        }
        String direcao = valor.trim().toUpperCase();
        if (!direcao.equals("NORTE") && !direcao.equals("SUL")
                && !direcao.equals("LESTE") && !direcao.equals("OESTE")) {
            throw new IllegalArgumentException("direção inválida. Use NORTE, SUL, LESTE ou OESTE");
        }
        return direcao;
    }

    @PrePersist
    public void prePersist() {
        if (this.direcao == null) {
            this.direcao = "NORTE";
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getDirecao() {
        return direcao;
    }

    public void setDirecao(String direcao) {
        this.direcao = normalizarDirecao(direcao);
    }
}
