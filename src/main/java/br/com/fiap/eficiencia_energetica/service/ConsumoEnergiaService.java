package br.com.fiap.eficienciaenergetica.service;

import br.com.fiap.eficienciaenergetica.dto.ConsumoEnergiaCadastroDTO;
import br.com.fiap.eficienciaenergetica.dto.ConsumoEnergiaExibicaoDTO;
import br.com.fiap.eficienciaenergetica.model.ConsumoEnergia;
import br.com.fiap.eficienciaenergetica.repository.ConsumoEnergiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<ConsumoEnergia> consumoOptional = repository.findById(id);
        if (consumoOptional.isPresent()) {
            return new ConsumoEnergiaExibicaoDTO(consumoOptional.get());
        }
        // Aqui o Controller vai tratar e devolver 404
        throw new RuntimeException("Consumo não encontrado!");
    }

    public void deletar(Long id) {
        Optional<ConsumoEnergia> consumoOptional = repository.findById(id);
        if (consumoOptional.isPresent()) {
            repository.delete(consumoOptional.get());
        } else {
            throw new RuntimeException("Consumo não encontrado!");
        }
    }
}
