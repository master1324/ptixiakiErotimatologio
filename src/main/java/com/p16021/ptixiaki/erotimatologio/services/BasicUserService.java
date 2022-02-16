package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.RegistrationRequest;
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
import java.util.List;

import static com.p16021.ptixiaki.erotimatologio.models.entities.user.Role.ROLE_USER;

@Service @RequiredArgsConstructor @Transactional
public class BasicUserService implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


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
    public RegistrationRequest register(RegistrationRequest request) {

        //TODO: validate request
        AppUser user = new AppUser(request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                ROLE_USER
                ,true,false);

        userRepo.save(user);

        return request;
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

    /*@Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser user = userRepo.findByUsername(username).get();
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }*/




}
