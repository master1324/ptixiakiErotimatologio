package com.p16021.ptixiaki.erotimatologio.models.entities.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@ToString
public class  RegistrationRequest {

    @NotBlank(message = "username cant be null")
    private final String username;
    @NotBlank(message = "password cant be null")
    private final String password;
    @NotBlank(message = "email cant be null")
    private final String email;
    private final String frontendLink;

}
