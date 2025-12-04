package com.gft.tag_ms.security;

import com.gft.tag_ms.exception.JwtAuthenticationException;
import com.gft.tag_ms.security.handle.CustomAuthenticationEntryPoint;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final CustomAuthenticationEntryPoint entryPoint;
    private final SecurityConfig securityConfig;

    public JwtFilter(CustomAuthenticationEntryPoint entryPoint, SecurityConfig securityConfig) {
        this.entryPoint = entryPoint;
        this.securityConfig = securityConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader(JWTParse.HEADER_AUTHORIZATION);

        String prefix = securityConfig.getPrefix();
        String secretKey = securityConfig.getKey();


        // Ignora paths que não precisam de token
        String path = request.getRequestURI();

        if (path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-ui.html") ||
                path.startsWith("/webjars") ||
                path.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }





        if (token == null || token.isBlank()) {
            log.warn("⚠️ Token ausente ou vazio no header Authorization");
            SecurityContextHolder.clearContext();

            entryPoint.commence(
                    request,
                    response,
                    new JwtAuthenticationException("Token ausente. Envie um token Bearer no header Authorization."));

            return; // <<< OBRIGATÓRIO!
        }

        try {
            log.info("🔹 Token recebido: [{}]", token);

            // 🔥 Parse do token usando JWTCreator
            JwtTokenData jwtData = JWTParse.parseToken(token, secretKey, prefix);

            log.info("✅ Token válido para usuário: [{}], roles: {}",
                    jwtData.getSubject(), jwtData.getRoles());

            // 🔥 Converte roles para authorities
            List<SimpleGrantedAuthority> authorities = jwtData.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            // 🔥 Cria objeto de autenticação
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(jwtData.getSubject(), null, authorities);


            // 🔥 Adiciona a autenticação no contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 🔥 Continua o fluxo da requisição no filtro
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            handleJwtException(request, response, "Token expirado. Faça login novamente.", e);
        } catch (MalformedJwtException e) {
            handleJwtException(request, response, "Token mal formado.", e);
        } catch (SecurityException e) {
            handleJwtException(request, response, "Assinatura do token inválida.", e);
        } catch (IllegalArgumentException e) {
            handleJwtException(request, response, "Token ausente ou inválido.", e);
        } catch (UnsupportedJwtException e) {
            handleJwtException(request, response, "Formato de token não suportado.", e);
        }
    }

    /**
     * Método helper para tratar exceções JWT de forma centralizada
     */
    private void handleJwtException(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String message,
                                    Exception e) throws IOException, ServletException {
        log.error("⚠️ {}", message, e);

        SecurityContextHolder.clearContext();
        entryPoint.commence(request, response, new JwtAuthenticationException(message));
    }
}