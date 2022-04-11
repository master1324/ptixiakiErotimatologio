package com.p16021.ptixiaki.erotimatologio.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.p16021.ptixiaki.erotimatologio.models.entities.user.RegistrationRequest;

import com.p16021.ptixiaki.erotimatologio.services.RegistrationService;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.AuthorizationTokenService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController @RequiredArgsConstructor @Slf4j
public class UserController {

    private final RegistrationService registrationService;
    private final AuthorizationTokenService authorizationTokenService;
    private final ObjectMapper mapper = new ObjectMapper();

//    @GetMapping("/is_admin")
//    public boolean isAdmin() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
//        log.info("Admin check " + isAdmin);
//        return isAdmin;
//    }
//
//    @GetMapping("/is_user")
//    public boolean isUser(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         boolean isUser = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
//        log.info("User check " + isUser);
//         return isUser;
//    }

    @GetMapping("/hasRole")
    public boolean harRole(@RequestParam String[] roles){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //boolean isUser = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(role));
        boolean isUser = authentication.getAuthorities().stream().anyMatch(auth -> Arrays.stream(roles).anyMatch(role ->auth.getAuthority().equals(role)));
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
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/signup").toUriString());
        String message;
        try{
            registrationService.register(request);
            return ResponseEntity.created(uri).build();
        }catch (IllegalStateException e){
            log.error(e.getMessage());
            message = e.getMessage();
        }

        return ResponseEntity.badRequest().body(message);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){

        return registrationService.confirmToken(token);
    }

    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response,@RequestBody String refreshToken) throws IOException {

        if (refreshToken != null){
            try {
                Map<String,String> tokens = authorizationTokenService.refreshAccessToken(refreshToken,request);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            }catch (Exception e){

                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> errors = new HashMap<>();
                errors.put("error_message xd", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }

        }else {
            throw new RuntimeException("token missing");
        }

    }

    @PostMapping("/logout")
    public void logout(@RequestBody String refreshToken){
    }

    private void throwError(String message,int status,HttpServletResponse response) throws IOException {

        response.setStatus(status);
        Map<String, Object> data = new HashMap<>();

        data.put(
                "exception",
                message);

        response.getOutputStream()
                .println(mapper.writeValueAsString(data));
    }
}
