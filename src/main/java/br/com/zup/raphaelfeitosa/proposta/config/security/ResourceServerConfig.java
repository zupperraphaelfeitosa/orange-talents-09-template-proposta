package br.com.zup.raphaelfeitosa.proposta.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
    private final String SCOPE = "SCOPE_api-proposta";

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests(
                        authorizerequests -> authorizerequests
                                .antMatchers(GET, "/actuator/**")
                                .permitAll()

                                .antMatchers(GET, "/api/v1/propostas/**")
                                .hasAuthority(SCOPE)

                                .antMatchers(POST, "/api/v1/propostas")
                                .hasAuthority(SCOPE)

                                .antMatchers(POST, "/api/v1/cartoes/**")
                                .hasAuthority(SCOPE)
                                .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf()
                .disable()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}