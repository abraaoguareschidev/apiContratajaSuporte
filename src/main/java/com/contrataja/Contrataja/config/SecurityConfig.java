package com.contrataja.Contrataja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// ativa a segurança web do spring security nesta aplicação as regras de acesso às rotas são definidas nesta classe
@EnableWebSecurity
// classe que define as regras de segurança da api, quem pode acessar o que
public class SecurityConfig {
    // registra o retorno deste método como um bean, um objeto gerenciado pelo spring, que ele usa automaticamente na aplicação
    @Bean
    // método que monta a cadeia de filtros de segurança que é por onde passa toda requisição antes de chegar nos controllers
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // desativa a proteção csrf, que protege contra requisições forjadas feitas a partir do navegador logado, em apis que não usam sessão ou cookies ela costuma ser desativada
                .csrf(csrf -> csrf.disable())
                // define as regras de autorização, anyRequest pega qualquer requisição e permitAll libera o acesso sem exigir login, por enquanto to deixando toda a api aberta
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        // constrói a cadeia de filtros com as regras definidas acima e devolve para o spring aplicar
        return http.build();
    }

}
