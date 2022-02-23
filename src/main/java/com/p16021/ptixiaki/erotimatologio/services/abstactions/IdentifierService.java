package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;

import java.util.List;
import java.util.Map;

public interface IdentifierService {
    Identifier findById(long id);

    Map<IdentifierType, List<Identifier>> findAll();

    List<String> findEligibleResponses(ResponseType responseType);

    List<Identifier> findByIdentifierType(IdentifierType identifierType);
}