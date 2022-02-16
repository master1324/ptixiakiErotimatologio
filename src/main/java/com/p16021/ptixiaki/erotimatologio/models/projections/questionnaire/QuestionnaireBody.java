package com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire;


import com.p16021.ptixiaki.erotimatologio.models.projections.QuestGroupView;

import java.util.List;

public interface QuestionnaireBody extends QuestionnaireView {

    long getId();
    String getName();
    List<QuestGroupView> getQuestionnaire();

}
