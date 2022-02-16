package com.p16021.ptixiaki.erotimatologio.models.projections;


import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;

import java.util.List;

public interface QuestGroupView {

    Long getId();
    String getName();
    List<QuestionView> getQuestions();
    ResponseType getResponseType();
    void setEligibleResponses(List<String> eligibleResponses);
    List<String> getEligibleResponses();
}
