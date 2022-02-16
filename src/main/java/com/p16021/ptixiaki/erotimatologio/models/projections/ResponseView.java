package com.p16021.ptixiaki.erotimatologio.models.projections;


import com.p16021.ptixiaki.erotimatologio.models.projections.result.QuestionIdView;

public interface ResponseView {

    Long getId();
    String getResponse();
    QuestionIdView getQuestion();
    String getFilter();

}
