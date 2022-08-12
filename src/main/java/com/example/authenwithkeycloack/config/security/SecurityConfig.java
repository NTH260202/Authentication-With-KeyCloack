package com.example.authenwithkeycloack.config.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
//        http.cors().and().csrf().disable().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
//                .antMatchers("/api/auth/***").permitAll()
//                .antMatchers("/api/users/***").hasAnyRole("account-owner")
//                .anyRequest().authenticated();
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/api/users/**").permitAll()
//                .antMatchers("/api/service/**").permitAll()
                .antMatchers("/member/**").hasAnyRole("member")
                .antMatchers("/moderator/**").hasAnyRole("moderator")
                .antMatchers("/admin/**").hasAnyRole("admin")
                .antMatchers("/service/**").permitAll()
                .anyRequest()
                .permitAll();

//        http.addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/configuration/security",
                "/webjars/**",
                "/csrf",
                "/swagger-ui.html",
                "/error"
        );
    }

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {

        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

//    @Bean
//    public CustomizationFilter customAuthorizationFilter() {
//        return new CustomizationFilter(tokenUtils);
//    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider =
                keycloakAuthenticationProvider();

        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());

        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

//
//    @Bean
//    public KeycloakConfigResolver KeycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }
}
