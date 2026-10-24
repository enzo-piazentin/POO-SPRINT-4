package br.com.motiva.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RELATORIO_PRIORIDADE")
public class RelatorioPrioridade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "URGENTES", nullable = false)
    private Integer urgentes;

    @Column(name = "CRITICOS", nullable = false)
    private Integer criticos;

    @Column(name = "ATENCAO", nullable = false)
    private Integer atencao;

    @Column(name = "NORMAIS", nullable = false)
    private Integer normais;

    @Column(name = "RESUMO", columnDefinition = "CLOB")
    private String resumo;

    @Column(name = "GERADO_EM", nullable = false, updatable = false)
    private LocalDateTime geradoEm;

    public RelatorioPrioridade() {
    }

    public RelatorioPrioridade(Integer urgentes, Integer criticos, Integer atencao, Integer normais, String resumo) {
        if (urgentes < 0 || criticos < 0 || atencao < 0 || normais < 0)
            throw new IllegalArgumentException("quantidades do relatório não podem ser negativas");
        this.urgentes = urgentes;
        this.criticos = criticos;
        this.atencao = atencao;
        this.normais = normais;
        this.resumo = resumo != null ? resumo : "";
        this.geradoEm = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.geradoEm == null) {
            this.geradoEm = LocalDateTime.now();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUrgentes() {
        return urgentes;
    }

    public void setUrgentes(Integer urgentes) {
        this.urgentes = urgentes;
    }

    public Integer getCriticos() {
        return criticos;
    }

    public void setCriticos(Integer criticos) {
        this.criticos = criticos;
    }

    public Integer getAtencao() {
        return atencao;
    }

    public void setAtencao(Integer atencao) {
        this.atencao = atencao;
    }

    public Integer getNormais() {
        return normais;
    }

    public void setNormais(Integer normais) {
        this.normais = normais;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public LocalDateTime getGeradoEm() {
        return geradoEm;
    }

    public void setGeradoEm(LocalDateTime geradoEm) {
        this.geradoEm = geradoEm;
    }
}
