package com.p16021.ptixiaki.erotimatologio.models.projections;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;

import javax.persistence.ElementCollection;
import java.util.Set;

public interface TeacherProjectionNoUserId {
    String getName();
    long getId();
    IdentifierType getType();
    Set<Identifier> getSubjects();
    Set<Identifier> getDepartments();
}
