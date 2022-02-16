package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.RegistrationRequest;

import java.util.List;

public interface UserService {

    RegistrationRequest register(RegistrationRequest request);
    /*Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);*/
    AppUser getUser(String Username);
    List<AppUser> getUsers();

}
