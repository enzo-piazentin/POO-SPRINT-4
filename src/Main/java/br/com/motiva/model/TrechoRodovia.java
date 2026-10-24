package br.com.motiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "TRECHO_RODOVIA")
public class TrechoRodovia {

    @Id
    @Column(name = "KM")
    private Integer km;

    @Column(name = "ALTURA_VEGETACAO", nullable = false, precision = 8, scale = 2)
    private Double alturaVegetacao;

    @NotNull(message = "tipoTerreno não pode ser nulo")
    @Column(name = "TIPO_TERRENO", nullable = false, length = 30)
    private String tipoTerreno;

    @Column(name = "EQUIPE_ID")
    private Integer equipeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQUIPE_ID", insertable = false, updatable = false)
    private EquipeManutencao equipe;

    public TrechoRodovia() {
    }

    public TrechoRodovia(Integer km, Double alturaVegetacao, String tipoTerreno) {
        if (km < 0) throw new IllegalArgumentException("km não pode ser negativo");
        if (alturaVegetacao < 0) throw new IllegalArgumentException("altura da vegetação não pode ser negativa");
        this.km = km;
        this.alturaVegetacao = alturaVegetacao;
        this.tipoTerreno = tipoTerreno;
    }

    public TrechoRodovia(Integer km, Double alturaVegetacao, String tipoTerreno, Integer equipeId) {
        this(km, alturaVegetacao, tipoTerreno);
        this.equipeId = equipeId;
    }

    public double calcularCrescimentoDiario() {
        if (tipoTerreno != null && tipoTerreno.equalsIgnoreCase("umido")) {
            return 5.0;
        }
        return 2.0;
    }

    public void atualizarVegetacao() {
        alturaVegetacao += calcularCrescimentoDiario();
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Double getAlturaVegetacao() {
        return alturaVegetacao;
    }

    public void setAlturaVegetacao(Double alturaVegetacao) {
        this.alturaVegetacao = alturaVegetacao;
    }

    public String getTipoTerreno() {
        return tipoTerreno;
    }

    public void setTipoTerreno(String tipoTerreno) {
        this.tipoTerreno = tipoTerreno;
    }

    public Integer getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(Integer equipeId) {
        this.equipeId = equipeId;
    }

    public EquipeManutencao getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeManutencao equipe) {
        this.equipe = equipe;
    }
}
