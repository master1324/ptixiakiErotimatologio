package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AuthorizationTokenService {


    Map<String,String> generateTokens(User user, HttpServletRequest request);
    Map<String,String> refreshAccessToken(String refreshToken,HttpServletRequest request);
}
