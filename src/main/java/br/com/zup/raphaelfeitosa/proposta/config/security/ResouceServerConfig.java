package br.com.zup.raphaelfeitosa.proposta.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
@EnableWebSecurity
public class ResouceServerConfig extends WebSecurityConfigurerAdapter {
    private final String scope = "SCOPE_api-proposta";

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.authorizeRequests(
                authorizerequests -> authorizerequests
                        .antMatchers(HttpMethod.GET, "/api/v1/propostas/**").hasAuthority(scope)
                        .antMatchers(HttpMethod.POST, "/api/v1/propostas").hasAuthority(scope)
                        .antMatchers(HttpMethod.POST, "/api/v1/biometrias/**").hasAuthority(scope)
                        .anyRequest().authenticated()
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}