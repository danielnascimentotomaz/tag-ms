package com.gft.tag_ms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/favicon.ico"
    };

    private final JwtFilter jwtFilter;

    public WebSecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // desabilita CSRF
                .authorizeHttpRequests(auth -> auth
                        // Swagger e H2 liberados
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()

                        // ROTAS → USER e ADMIN
                        .requestMatchers(HttpMethod.GET, "/tags").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/tags").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET,"/tags/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET,"/tags/exists/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET,"/tags/{id}/words").hasAnyRole("USER", "ADMIN")

                        // ROTAS DE ESCRITA → ADMIN
                        .requestMatchers(HttpMethod.PUT, "/tags/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tags/{id}").hasRole("ADMIN")

                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // necessário para H2 Console
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}