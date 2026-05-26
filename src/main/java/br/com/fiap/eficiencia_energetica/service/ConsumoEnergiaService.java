package br.com.fiap.eficiencia_energetica.service;

import br.com.fiap.eficiencia_energetica.dto.ConsumoEnergiaCadastroDTO;
import br.com.fiap.eficiencia_energetica.dto.ConsumoEnergiaExibicaoDTO;
import br.com.fiap.eficiencia_energetica.model.ConsumoEnergia;
import br.com.fiap.eficiencia_energetica.repository.ConsumoEnergiaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumoEnergiaService {

    @Autowired
    private ConsumoEnergiaRepository repository;

    public ConsumoEnergiaExibicaoDTO registrarConsumo(ConsumoEnergiaCadastroDTO dto) {
        ConsumoEnergia consumo = new ConsumoEnergia();
        consumo.setNomeEquipamento(dto.nomeEquipamento());
        consumo.setSetor(dto.setor());
        consumo.setConsumoKwh(dto.consumoKwh());
        consumo.setDataRegistro(dto.dataRegistro());

        ConsumoEnergia consumoSalvo = repository.save(consumo);
        return new ConsumoEnergiaExibicaoDTO(consumoSalvo);
    }

    public List<ConsumoEnergiaExibicaoDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(ConsumoEnergiaExibicaoDTO::new)
                .toList();
    }

    public ConsumoEnergiaExibicaoDTO buscarPorId(Long id) {
        // Se não achar o ID, lança a exceção que o TratadorDeErros está esperando
        ConsumoEnergia consumo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consumo não encontrado!"));

        return new ConsumoEnergiaExibicaoDTO(consumo);
    }

    public void deletar(Long id) {
        ConsumoEnergia consumo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consumo não encontrado!"));

        repository.delete(consumo);
    }
}