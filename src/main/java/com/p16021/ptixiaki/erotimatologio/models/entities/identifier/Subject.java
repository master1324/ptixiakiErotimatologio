package com.p16021.ptixiaki.erotimatologio.models.entities.identifier;

import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Set;

import static com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType.SUBJECT;

//@Entity
//@NoArgsConstructor
//@Getter @Setter
public class Subject extends Identifier{

//    public Subject(String name, IdentifierType type, Department department, List<Teacher> teachers) {
//        super(name, SUBJECT);
//        this.department = department;
//        this.teachers = teachers;
//    }
//
//    @ManyToOne
//    @JoinColumn(name = "department_id ")
//    Department department;
//
//    @ManyToMany
//    List<Teacher> teachers;
}
