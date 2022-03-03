package com.p16021.ptixiaki.erotimatologio.security;

import com.p16021.ptixiaki.erotimatologio.security.filters.CustomAuthenticationFilter;
import com.p16021.ptixiaki.erotimatologio.security.filters.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //TODO: ftiakse kalitera to security
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.cors().and().authorizeRequests().antMatchers("/login/**","/signup/**","/refresh/**","/confirm/**").permitAll();
        http.authorizeRequests().antMatchers("/response/**").hasAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(PUT,"/quest/{qid}").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/quest/add").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE,"/quest/{qid}").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/quest/{qid}/results").hasAuthority("ROLE_ADMIN");

        http.authorizeRequests().antMatchers(GET,"/roles","/is_user","/is_admin","/quest/{qid}","/quest/all","/identifiers/all")
                .hasAnyAuthority("ROLE_USER","ROLE_ADMIN");

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/


}
