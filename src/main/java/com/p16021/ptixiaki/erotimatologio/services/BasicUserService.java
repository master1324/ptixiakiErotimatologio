package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.ConfirmationToken;
import com.p16021.ptixiaki.erotimatologio.repos.UserRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor @Transactional
public class BasicUserService implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ConformationTokenService conformationTokenService;

    //TODO: make login only to enabled users
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("username not found"));

        /*Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));*/

        //TODO: check if puting user id instead of username suits you
        return new User(String.valueOf(user.getId()),user.getPassword(),user.getAuthorities());
    }

    //TODO: make username unique etc
    @Override
    public String register(AppUser user) {

        boolean userExists = userRepo.findByEmail(user.getEmail()).isPresent();

        if(userExists){
            throw new IllegalStateException("email taken");
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
    public AppUser getUser(String username) {
        return userRepo.findById(Long.valueOf(username)).get();
    }

    //TODO: remove unused methods
    @Override
    public List<AppUser> getUsers() {
        return (List<AppUser>) userRepo.findAll();
    }




}
