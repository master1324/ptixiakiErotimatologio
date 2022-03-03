package com.p16021.ptixiaki.erotimatologio.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.RegistrationRequest;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.Role;
import com.p16021.ptixiaki.erotimatologio.services.RegistrationService;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController @RequiredArgsConstructor @Slf4j
public class UserController {

    private final RegistrationService registrationService;
    private final UserService userService;

    @GetMapping("/is_admin")
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        log.info("Admin check " + isAdmin);
        return isAdmin;
    }

    @GetMapping("/is_user")
    public boolean isUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         boolean isUser = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
        log.info("User check " + isUser);
         return isUser;
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<String>> getRoles(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return ResponseEntity.ok().body(roles);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest request){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/signup").toUriString());
        return ResponseEntity.created(uri).body(registrationService.register(request));
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){

        return registrationService.confirmToken(token);
    }

    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response,@RequestBody String refreshToken) throws IOException {

        //String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (refreshToken != null){
            try {
                //String refreshToken = refresh_token;
                //TODO ftiakse util class pou kanei handle tin idia douleia
                Algorithm algorithm = Algorithm.HMAC256("xd".getBytes());

                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                AppUser user = userService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(String.valueOf(user.getId()))
                        .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 *1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",Arrays.asList(user.getRole().name()))
                        .sign(algorithm);


                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);


            }catch (Exception e){


                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String,String> errors = new HashMap<>();
                errors.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }

        }else {
            throw new RuntimeException("token missing");
        }

    }

    @PostMapping("/logout")
    public void logout(@RequestBody String refreshToken){
        //TODO: apothikeuse ola ta refresh token se database kai kanto delete
    }
}
