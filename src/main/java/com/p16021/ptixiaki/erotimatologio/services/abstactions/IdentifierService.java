package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;

import java.util.List;
import java.util.Map;

public interface IdentifierService {
    Identifier findById(long id);

    List<Identifier> findAll();
    Map<IdentifierType, List<Identifier>> findAllGrouped();
    Map<IdentifierType, List<Identifier>> findAllByUser(long userId);

    List<String> findEligibleResponses(ResponseType responseType);

    List<Identifier> findByIdentifierType(IdentifierType identifierType,long userId);
}
