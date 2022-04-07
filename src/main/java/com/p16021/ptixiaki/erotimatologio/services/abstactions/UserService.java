package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.UpdateProfileRequest;
import com.p16021.ptixiaki.erotimatologio.models.projections.AppUserProjection;

import java.util.List;

public interface UserService {

    String register(AppUser user);
    /*Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);*/
    AppUser getUser(String Username);
    AppUserProjection getUserById(long id);
    int enableAppUser(String email);
    void updateUser(UpdateProfileRequest request,long userId);
}
