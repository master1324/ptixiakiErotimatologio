package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.ConfirmationToken;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.UpdateProfileRequest;
import com.p16021.ptixiaki.erotimatologio.models.projections.AppUserProjection;
import com.p16021.ptixiaki.erotimatologio.repos.UserRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.p16021.ptixiaki.erotimatologio.models.entities.user.Role.ROLE_TEACHER;


@Service @RequiredArgsConstructor @Transactional @Slf4j
public class BasicUserService implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ConformationTokenService conformationTokenService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("username not found"));

        if(!user.isEnabled()){
            throw new UsernameNotFoundException("user is not enabled");
        }

        /*Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));*/

        return new User(String.valueOf(user.getId()),user.getPassword(),user.getAuthorities());
    }

    @Override
    @Transactional
    public String register(AppUser user) {

        boolean userExists = userRepo.findByEmail(user.getEmail()).isPresent();
        boolean usernameExists = userRepo.findByUsername(user.getUsername()).isPresent();

        if(userExists){
            throw new IllegalStateException("?????????????? ???????? ?????????????????????? ???? ???????? ???? email");
        }

        if(usernameExists){
            throw new IllegalStateException("?????????????? ???????? ?????????????????????? ???? ???????? ???? ?????????? ????????????");
        }

        userRepo.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            user
        );

        conformationTokenService.saveConformationToken(confirmationToken);
        return token;
    }

    @Override
    public int enableAppUser(String email) {
        return userRepo.enableAppUser(email);
    }

    @Override
    public void updateUser(UpdateProfileRequest request,long userId) {

        Optional<AppUser> appUserOptional = userRepo.findById(userId);


        if(!appUserOptional.isPresent()){
            throw new RuntimeException("Sfalma");
        }
        boolean passwordsMatch = passwordEncoder.matches(request.getOldPassword(), appUserOptional.get().getPassword());

        if(!passwordsMatch){
            throw new RuntimeException("Lathos kodikos");
        }

        userRepo.save(new AppUser(
                userId,
                request.getUsername(),
                passwordEncoder.encode(request.getNewPassword()),
                request.getEmail(),
                true,
                appUserOptional.get().getRole()));
    }

    @Override
    public AppUser getUser(String username) {
        return userRepo.findById(Long.valueOf(username)).get();
    }

    @Override
    public AppUserProjection getUserById(long id) {

        Optional<AppUserProjection> optional =userRepo.findProjectedById(id);
        if (optional.isPresent()){

            return optional.get();
        }else {
            throw new RuntimeException("user not found xd");
        }

    }


}
