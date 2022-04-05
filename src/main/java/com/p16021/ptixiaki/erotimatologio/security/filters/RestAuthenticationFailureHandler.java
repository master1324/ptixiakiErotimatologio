package com.p16021.ptixiaki.erotimatologio.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.ConfirmationToken;
import com.p16021.ptixiaki.erotimatologio.repos.ConformationTokenRepo;
import com.p16021.ptixiaki.erotimatologio.repos.UserRepo;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UserRepo userRepo;
    private final ConformationTokenRepo confirmationTokenRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");
        Optional<AppUser> user = userRepo.findByUsername(username);
        log.info(exception.toString());
        if(user.isPresent()){
            if(!user.get().isEnabled()){
                log.info(exception.getMessage());
                throwError("Δεν έχετε ενεργοποιήσει το email σας",UNAUTHORIZED.value(),response);
            }else{
                throwError("Λάθος όνομα χρήστη / κωδικός",UNAUTHORIZED.value(),response);
            }
        }else{
            throwError("Λάθος όνομα χρήστη / κωδικός",UNAUTHORIZED.value(),response);
        }
    }


    private void throwError(String message,int status,HttpServletResponse response) throws IOException {
        response.setStatus(status);
        //response.setContentType("text/event-stream; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> data = new HashMap<>();

        data.put(
                "exception",
                message);

        response.getWriter()
                .println(mapper.writeValueAsString(data));
    }
}
