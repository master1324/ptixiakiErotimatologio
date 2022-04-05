package com.p16021.ptixiaki.erotimatologio.models.entities.identifier;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType.TEACHER;

@Entity
@NoArgsConstructor
@Getter@Setter
public class Teacher extends Identifier {

    @ElementCollection
    private Set<Identifier> subjects;
    @ElementCollection
    private Set<Identifier> departments;
    private Long appUserId;

    public Teacher(String name){
        super(name,TEACHER);
    }

    public Teacher(String name ,Set<Identifier> subjects, Set<Identifier> departments, Long appUserId) {
        super(name, TEACHER);
        this.subjects = subjects;
        this.departments = departments;
        this.appUserId = appUserId;
    }

    public Teacher(String name ,Set<Identifier> subjects, Set<Identifier> departments) {
        super(name, TEACHER);
        this.subjects = subjects;
        this.departments = departments;

    }

}
