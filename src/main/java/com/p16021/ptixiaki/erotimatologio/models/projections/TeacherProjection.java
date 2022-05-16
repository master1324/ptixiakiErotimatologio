package com.p16021.ptixiaki.erotimatologio.models.projections;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;

public interface TeacherProjection {

    String getName();
    long getId();
    IdentifierType getType();
}
