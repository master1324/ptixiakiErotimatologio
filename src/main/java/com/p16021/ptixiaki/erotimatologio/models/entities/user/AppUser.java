package com.p16021.ptixiaki.erotimatologio.models.entities.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;

@Entity @Data
@NoArgsConstructor @AllArgsConstructor
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "username cant be null")
    private String username;
    @NotBlank(message = "password cant be null")
    private String password;
    @NotBlank(message = "email cant be null")
    private String email;

    private Role role;
    private Boolean enabled;
    private Boolean locked;

    public AppUser(String username, String password, String email, Role role, Boolean enabled, Boolean locked) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
        this.locked = locked;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        roles.forEach(role ->{
//            authorities.add(new SimpleGrantedAuthority(role.toString()));
//        });
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
