package com.p16021.ptixiaki.erotimatologio.models.projections.result;

import java.util.List;

public interface QuestionnaireResult {

    String getName();
    List<QuestGroupResultView> getQuestionnaire();
    //void setNumOfResponses(int numOfResponses);
    //int getNumOfResponses();
}
