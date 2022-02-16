package com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire;


import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Identifier {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;
    private IdentifierType type;


    public Identifier(String name, IdentifierType type) {
        this.name = name;
        this.type = type;
    }

    public Identifier(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IdentifierType getType() {
        return type;
    }

    public void setType(IdentifierType type) {
        this.type = type;
    }


}
