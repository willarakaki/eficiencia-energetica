package br.com.fiap.eficiencia_energetica.service;

import br.com.fiap.eficiencia_energetica.dto.ConsumoEnergiaCadastroDTO;
import br.com.fiap.eficiencia_energetica.dto.ConsumoEnergiaExibicaoDTO;
import br.com.fiap.eficiencia_energetica.model.ConsumoEnergia;
import br.com.fiap.eficiencia_energetica.repository.ConsumoEnergiaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsumoEnergiaServiceTest {

    @Mock
    private ConsumoEnergiaRepository repository;

    @InjectMocks
    private ConsumoEnergiaService service;

    @Test
    @DisplayName("Deve registrar consumo de energia com sucesso")
    void deveRegistrarConsumoComSucesso() {
        ConsumoEnergiaCadastroDTO dto = new ConsumoEnergiaCadastroDTO(
                "Ar Condicionado Central",
                "Bloco A",
                150.5,
                LocalDate.now()
        );

        ConsumoEnergia consumoSalvo = new ConsumoEnergia(
                1L,
                dto.nomeEquipamento(),
                dto.setor(),
                dto.consumoKwh(),
                dto.dataRegistro()
        );

        when(repository.save(any(ConsumoEnergia.class))).thenReturn(consumoSalvo);

        ConsumoEnergiaExibicaoDTO resultado = service.registrarConsumo(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Ar Condicionado Central", resultado.nomeEquipamento());
        assertEquals("Bloco A", resultado.setor());
        assertEquals(150.5, resultado.consumoKwh());
        verify(repository, times(1)).save(any(ConsumoEnergia.class));
    }

    @Test
    @DisplayName("Deve listar todos os registros de consumo")
    void deveListarTodos() {
        ConsumoEnergia c1 = new ConsumoEnergia(1L, "Painel Solar 1", "Telhado", 320.0, LocalDate.now());
        ConsumoEnergia c2 = new ConsumoEnergia(2L, "Iluminação LED", "Pátio", 45.0, LocalDate.now());

        when(repository.findAll()).thenReturn(List.of(c1, c2));

        List<ConsumoEnergiaExibicaoDTO> lista = service.listarTodos();

        assertEquals(2, lista.size());
        assertEquals("Painel Solar 1", lista.get(0).nomeEquipamento());
        assertEquals("Iluminação LED", lista.get(1).nomeEquipamento());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar registro existente por ID")
    void deveBuscarPorIdExistente() {
        ConsumoEnergia c = new ConsumoEnergia(1L, "Bomba d'água", "Subsolo", 75.0, LocalDate.now());
        when(repository.findById(1L)).thenReturn(Optional.of(c));

        ConsumoEnergiaExibicaoDTO resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Bomba d'água", resultado.nomeEquipamento());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando ID não existir")
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(999L));
        verify(repository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve deletar registro com sucesso")
    void deveDeletarComSucesso() {
        ConsumoEnergia c = new ConsumoEnergia(1L, "Gerador", "Térreo", 120.0, LocalDate.now());
        when(repository.findById(1L)).thenReturn(Optional.of(c));
        doNothing().when(repository).delete(c);

        service.deletar(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(c);
    }
}
