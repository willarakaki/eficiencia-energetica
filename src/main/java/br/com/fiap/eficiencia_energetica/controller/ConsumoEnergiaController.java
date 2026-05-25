package br.com.fiap.eficienciaenergetica.controller;

import br.com.fiap.eficienciaenergetica.dto.ConsumoEnergiaCadastroDTO;
import br.com.fiap.eficienciaenergetica.dto.ConsumoEnergiaExibicaoDTO;
import br.com.fiap.eficienciaenergetica.service.ConsumoEnergiaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumo-energia")
public class ConsumoEnergiaController {

    @Autowired
    private ConsumoEnergiaService service;

    // 1. POST - Registrar novo consumo
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsumoEnergiaExibicaoDTO registrar(@RequestBody @Valid ConsumoEnergiaCadastroDTO dto) {
        return service.registrarConsumo(dto);
    }

    // 2. GET - Listar todos
    @GetMapping
    public ResponseEntity<List<ConsumoEnergiaExibicaoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // 3. GET - Buscar por ID com tratamento de exceção (404 Not Found)
    @GetMapping("/{id}")
    public ResponseEntity<ConsumoEnergiaExibicaoDTO> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Retorna 404 caso o Service lance a exceção
        }
    }

    // 4. DELETE - Excluir registro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content após deletar
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
