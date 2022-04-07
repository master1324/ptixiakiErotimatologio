package com.p16021.ptixiaki.erotimatologio.models.entities.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class UpdateProfileRequest {

    private String username;
    private String oldPassword;
    private String newPassword;
    private String email;
}
