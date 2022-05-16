package com.p16021.ptixiaki.erotimatologio.models.entities.user;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

import static com.p16021.ptixiaki.erotimatologio.models.entities.user.Role.ROLE_USER;
import static java.util.Arrays.stream;

@Entity @Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Slf4j
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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

    private Role role = ROLE_USER;
    private Boolean enabled = false;
    private Boolean locked = false;

    public AppUser(String username, String password, String email, Role role, Boolean enabled, Boolean locked) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
        this.locked = locked;
    }

    public AppUser(long id,String username, String password, String email, Boolean enabled,Role role) {
        this.id =id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.role =role;
    }

    public AppUser(String username, String password, String email, Boolean enabled,Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.role =role;
    }

    public AppUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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
