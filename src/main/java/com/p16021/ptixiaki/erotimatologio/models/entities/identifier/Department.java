package com.p16021.ptixiaki.erotimatologio.models.entities.identifier;

import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

import static com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType.DEPARTMENT;

//@Entity
//@NoArgsConstructor
//@Getter@Setter
public class Department extends Identifier{

//    public Department(String name, IdentifierType type, List<Teacher> teachers, List<Subject> subjects) {
//        super(name, DEPARTMENT);
//        this.teachers = teachers;
//        this.subjects = subjects;
//    }

//    @ManyToMany
//    List<Teacher> teachers;
//
//    @OneToMany(mappedBy = "department")
//    List<Subject> subjects;
}
