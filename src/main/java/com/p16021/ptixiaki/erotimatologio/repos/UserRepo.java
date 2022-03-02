package com.p16021.ptixiaki.erotimatologio.repos;

import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepo extends CrudRepository<AppUser,Long> {

    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);
}
