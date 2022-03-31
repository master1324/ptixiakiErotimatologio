package com.p16021.ptixiaki.erotimatologio.models.entities.identifier;

import com.fasterxml.jackson.annotation.JsonBackReference;
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



    //private List<Identifier> departments;

    @ElementCollection
    private List<Identifier> subjects;
    private Long appUserId;

    public Teacher(String name){
        super(name,TEACHER);
    }
}
