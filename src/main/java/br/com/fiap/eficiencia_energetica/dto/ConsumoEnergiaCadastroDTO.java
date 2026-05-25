package br.com.fiap.eficiencia_energetica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ConsumoEnergiaCadastroDTO(
        @NotBlank(message = "O nome do equipamento é obrigatório")
        String nomeEquipamento,

        @NotBlank(message = "O setor é obrigatório")
        String setor,

        @NotNull(message = "O consumo é obrigatório")
        @Positive(message = "O consumo deve ser maior que zero")
        Double consumoKwh,

        @NotNull(message = "A data de registro é obrigatória")
        LocalDate dataRegistro
) {}
