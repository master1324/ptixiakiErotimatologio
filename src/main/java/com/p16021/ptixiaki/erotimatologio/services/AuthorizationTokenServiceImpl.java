package com.p16021.ptixiaki.erotimatologio.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.AuthorizationTokenService;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service@RequiredArgsConstructor
public class AuthorizationTokenServiceImpl implements AuthorizationTokenService {

    private final UserService userService;

    @Override
    public Map<String, String> generateTokens(User user, HttpServletRequest request) {

        Algorithm algorithm = loadSecret();

        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 *1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 6000 * 60 *1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        return tokens;
    }

    @Override
    public Map<String,String> refreshAccessToken(String refreshToken,HttpServletRequest request) {

        Algorithm algorithm = loadSecret();

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        AppUser user = userService.getUser(username);

        String access_token = JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 *1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", List.of(user.getRole().name()))
                .sign(algorithm);

        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    @Override
    public DecodedJWT decodeToken(String token) {

        JWTVerifier verifier = JWT.require(loadSecret()).build();

        return verifier.verify(token);
    }

    private Algorithm loadSecret() {
        return Algorithm.HMAC256("xd".getBytes());
    }
}
