package br.com.fiap.eficienciaenergetica.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "TBL_CONSUMO_ENERGIA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ConsumoEnergia {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_CONSUMO_ENERGIA"
    )
    @SequenceGenerator(
            name = "SEQ_CONSUMO_ENERGIA",
            sequenceName = "SEQ_CONSUMO_ENERGIA",
            allocationSize = 50
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_equipamento", nullable = false)
    private String nomeEquipamento;

    @Column(name = "setor", nullable = false)
    private String setor;

    @Column(name = "consumo_kwh", nullable = false)
    private Double consumoKwh;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;
}