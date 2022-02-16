package com.p16021.ptixiaki.erotimatologio.models.projections.result;

import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;

import java.util.List;

public interface QuestGroupResultView {
    String getName();
    List<QuestionResultView> getQuestions();
    String getResponseType();
}
