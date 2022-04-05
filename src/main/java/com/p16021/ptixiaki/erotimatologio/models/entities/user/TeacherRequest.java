package com.p16021.ptixiaki.erotimatologio.models.entities.user;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter@Setter
@NoArgsConstructor
public class TeacherRequest {

    private String name;
    private String username;
    private String email;
    private Set<Identifier> subjects;
    private Set<Identifier> departments;
}
