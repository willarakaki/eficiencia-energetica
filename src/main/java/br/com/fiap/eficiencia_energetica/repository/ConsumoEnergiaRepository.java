package br.com.fiap.eficiencia_energetica.repository;

import br.com.fiap.eficiencia_energetica.model.ConsumoEnergia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumoEnergiaRepository extends JpaRepository<ConsumoEnergia, Long> {
}