package br.com.motiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "INTERVENCAO_OPERACIONAL")
public class IntervencaoOperacional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "km do trecho é obrigatório")
    @Column(name = "KM_TRECHO", nullable = false)
    private Integer kmTrecho;

    @NotBlank(message = "tipo da intervenção é obrigatório")
    @Column(name = "TIPO", nullable = false, length = 60)
    private String tipo;

    @NotBlank(message = "responsável é obrigatório")
    @Column(name = "RESPONSAVEL", nullable = false, length = 100)
    private String responsavel;

    @Column(name = "DATA_INTERVENCAO", nullable = false)
    private LocalDate dataIntervencao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KM_TRECHO", insertable = false, updatable = false)
    private TrechoRodovia trecho;

    public IntervencaoOperacional() {
    }

    public IntervencaoOperacional(Integer kmTrecho, String tipo, String responsavel) {
        this.kmTrecho = kmTrecho;
        this.tipo = tipo;
        this.responsavel = responsavel;
        this.dataIntervencao = LocalDate.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKmTrecho() {
        return kmTrecho;
    }

    public void setKmTrecho(Integer kmTrecho) {
        this.kmTrecho = kmTrecho;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public LocalDate getDataIntervencao() {
        return dataIntervencao;
    }

    public void setDataIntervencao(LocalDate dataIntervencao) {
        this.dataIntervencao = dataIntervencao;
    }

    public TrechoRodovia getTrecho() {
        return trecho;
    }

    public void setTrecho(TrechoRodovia trecho) {
        this.trecho = trecho;
    }
}
