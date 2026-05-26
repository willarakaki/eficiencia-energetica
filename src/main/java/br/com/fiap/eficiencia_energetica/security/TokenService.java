package br.com.fiap.eficiencia_energetica.security;

import br.com.fiap.eficiencia_energetica.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Eficiencia Energetica")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API Eficiencia Energetica")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    // private Instant gerarDataExpiracao() {
    //        // Token expira em 2 horas
    //        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    //    }

    private Instant gerarDataExpiracao() {
        // Pega o momento atual exato (em UTC padrão) e soma 2 horas, sem brigar com fusos horários locais
        return Instant.now().plus(2, java.time.temporal.ChronoUnit.HOURS);
    }
}
