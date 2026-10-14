package br.com.fiap.eficiencia_energetica.controller;

import br.com.fiap.eficiencia_energetica.dto.ConsumoEnergiaCadastroDTO;
import br.com.fiap.eficiencia_energetica.dto.ConsumoEnergiaExibicaoDTO;
import br.com.fiap.eficiencia_energetica.repository.UsuarioRepository;
import br.com.fiap.eficiencia_energetica.security.SecurityFilter;
import br.com.fiap.eficiencia_energetica.security.TokenService;
import br.com.fiap.eficiencia_energetica.service.ConsumoEnergiaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsumoEnergiaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConsumoEnergiaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsumoEnergiaService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    @DisplayName("Deve retornar 201 Created ao registrar consumo com dados válidos")
    void deveRegistrarConsumoComSucesso() throws Exception {
        ConsumoEnergiaExibicaoDTO retorno = new ConsumoEnergiaExibicaoDTO(
                1L, "Painel Solar", "Setor A", 250.0, LocalDate.now()
        );

        when(service.registrarConsumo(any(ConsumoEnergiaCadastroDTO.class))).thenReturn(retorno);

        String json = """
                {
                    "nomeEquipamento": "Painel Solar",
                    "setor": "Setor A",
                    "consumoKwh": 250.0,
                    "dataRegistro": "2026-10-13"
                }
                """;

        mockMvc.perform(post("/consumo-energia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeEquipamento").value("Painel Solar"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao registrar consumo com dados inválidos")
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        String jsonInvalido = """
                {
                    "nomeEquipamento": "",
                    "setor": "",
                    "consumoKwh": -10.0,
                    "dataRegistro": null
                }
                """;

        mockMvc.perform(post("/consumo-energia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao listar todos os consumos")
    void deveListarTodos() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(
                new ConsumoEnergiaExibicaoDTO(1L, "Bomba 1", "Subsolo", 100.0, LocalDate.now())
        ));

        mockMvc.perform(get("/consumo-energia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeEquipamento").value("Bomba 1"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao buscar ID inexistente")
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        when(service.buscarPorId(999L)).thenThrow(new EntityNotFoundException("Consumo não encontrado!"));

        mockMvc.perform(get("/consumo-energia/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao excluir com sucesso")
    void deveRetornar204AoExcluir() throws Exception {
        mockMvc.perform(delete("/consumo-energia/1"))
                .andExpect(status().isNoContent());
    }
}
