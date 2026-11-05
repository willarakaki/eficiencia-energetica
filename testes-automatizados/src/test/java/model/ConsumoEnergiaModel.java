package model;

import com.google.gson.annotations.Expose;

public class ConsumoEnergiaModel {
    @Expose(serialize = false)
    private Long id;

    @Expose
    private String nomeEquipamento;

    @Expose
    private String setor;

    @Expose
    private Double consumoKwh;

    @Expose
    private String dataRegistro;

    public ConsumoEnergiaModel() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeEquipamento() {
        return nomeEquipamento;
    }

    public void setNomeEquipamento(String nomeEquipamento) {
        this.nomeEquipamento = nomeEquipamento;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public Double getConsumoKwh() {
        return consumoKwh;
    }

    public void setConsumoKwh(Double consumoKwh) {
        this.consumoKwh = consumoKwh;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
