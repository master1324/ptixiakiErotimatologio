package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;

import java.util.List;

public interface UserService {

    String register(AppUser user);
    /*Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);*/
    AppUser getUser(String Username);

    int enableAppUser(String email);
}
