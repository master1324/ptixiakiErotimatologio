package com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire;


import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QuestionnaireIdentifiers extends QuestionnaireView {

    long getId();
    String getName();
    Set<IdentifierType> getIdentifiers();
    void setEligibleResponsesIdentifiers(Map<IdentifierType, List<Identifier>> map);
    Map<IdentifierType, List<Identifier>> getEligibleResponsesIdentifiers();

}
