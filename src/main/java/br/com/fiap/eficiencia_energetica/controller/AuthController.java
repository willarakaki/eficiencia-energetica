package br.com.fiap.eficiencia_energetica.controller;

import br.com.fiap.eficiencia_energetica.dto.LoginDTO;
import br.com.fiap.eficiencia_energetica.dto.TokenDTO;
import br.com.fiap.eficiencia_energetica.dto.UsuarioCadastroDTO;
import br.com.fiap.eficiencia_energetica.model.Usuario;
import br.com.fiap.eficiencia_energetica.repository.UsuarioRepository;
import br.com.fiap.eficiencia_energetica.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> efetuarLogin(@RequestBody @Valid LoginDTO dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        // Gera o token JWT
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenDTO(tokenJWT));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody @Valid UsuarioCadastroDTO dados) {
        // Verifica se o e-mail já existe no banco
        if(this.repository.findByEmail(dados.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(dados.senha());

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dados.nome());
        novoUsuario.setEmail(dados.email());
        novoUsuario.setSenha(senhaCriptografada);
        novoUsuario.setRole(dados.role());

        this.repository.save(novoUsuario);

        return ResponseEntity.ok().build();
    }
}