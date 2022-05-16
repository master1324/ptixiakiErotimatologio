package com.p16021.ptixiaki.erotimatologio.security;

import com.p16021.ptixiaki.erotimatologio.repos.ConformationTokenRepo;
import com.p16021.ptixiaki.erotimatologio.repos.UserRepo;
import com.p16021.ptixiaki.erotimatologio.security.filters.CustomAuthenticationFilter;
import com.p16021.ptixiaki.erotimatologio.security.filters.CustomAuthorizationFilter;
import com.p16021.ptixiaki.erotimatologio.security.filters.RestAuthenticationFailureHandler;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.AuthorizationTokenService;
import lombok.RequiredArgsConstructor;
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

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepo userRepo;
    private final ConformationTokenRepo confirmationTokenRepo;
    private final AuthorizationTokenService authorizationTokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.cors().and().authorizeRequests().antMatchers("/login/**",
                "/signup/**","/refresh/**","/confirm/**","/v2/user/add").permitAll();
        http.authorizeRequests().antMatchers("/response/**","/v2/response/**").hasAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST,"/quest/add","/v2/quest/add").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/teacher/**","/v2/teacher/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/quest/{qid}/results","/v2/quest/{id}/results")
                .hasAnyAuthority("ROLE_ADMIN","ROLE_TEACHER");
        http.authorizeRequests().antMatchers("/filter/decode")
                .hasAnyAuthority("ROLE_USER","ROLE_TEACHER","ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/filter/**","/v2/filter/**")
                .hasAnyAuthority("ROLE_ADMIN","ROLE_TEACHER");
        http.authorizeRequests().antMatchers(GET,"/roles","/is_user","/is_admin","/quest/{qid}",
                        "/v2/quest/{id}","/quest/all","/v2/quest/all","/identifiers/all","/v2/identifiers/all")
                .hasAnyAuthority("ROLE_USER","ROLE_ADMIN","ROLE_TEACHER");

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter());
        http.addFilterBefore(new CustomAuthorizationFilter(authorizationTokenService),
                UsernamePasswordAuthenticationFilter.class);
        http.httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception{
        final CustomAuthenticationFilter filter =
                new CustomAuthenticationFilter(authenticationManagerBean(),
                        authorizationTokenService);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    RestAuthenticationFailureHandler authenticationFailureHandler(){
        return new RestAuthenticationFailureHandler(userRepo,confirmationTokenRepo);
    }


}
