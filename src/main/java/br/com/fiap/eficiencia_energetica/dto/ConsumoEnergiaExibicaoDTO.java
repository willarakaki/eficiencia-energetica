package br.com.fiap.eficiencia_energetica.dto;

import br.com.fiap.eficiencia_energetica.model.ConsumoEnergia;
import java.time.LocalDate;

public record ConsumoEnergiaExibicaoDTO(
        Long id,
        String nomeEquipamento,
        String setor,
        Double consumoKwh,
        LocalDate dataRegistro
) {
    // Construtor auxiliar para converter a Entidade em DTO automaticamente
    public ConsumoEnergiaExibicaoDTO(ConsumoEnergia consumo) {
        this(
                consumo.getId(),
                consumo.getNomeEquipamento(),
                consumo.getSetor(),
                consumo.getConsumoKwh(),
                consumo.getDataRegistro()
        );
    }
}