package com.p16021.ptixiaki.erotimatologio.models.projections;

import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;

public interface IdentifierListProjection {

    String getName();
    long getId();
    IdentifierType getType();
}
