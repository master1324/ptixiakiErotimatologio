package com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire;


import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestGroupView;

import java.util.List;
import java.util.Set;

public interface QuestionnaireBody extends QuestionnaireView {

    long getId();
    String getName();
     Set<IdentifierType> getIdentifiers();
    boolean getEnabled();
    List<QuestGroupView> getQuestionnaire();

}
