package com.p16021.ptixiaki.erotimatologio.models.projections;

import com.p16021.ptixiaki.erotimatologio.models.entities.user.Role;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import static com.p16021.ptixiaki.erotimatologio.models.entities.user.Role.ROLE_USER;

public interface AppUserProjection {

    String getUsername();
    String getEmail();

}
