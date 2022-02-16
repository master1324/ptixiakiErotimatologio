package com.p16021.ptixiaki.erotimatologio.models.projections.result;

import org.springframework.beans.factory.annotation.Value;

public interface QuestionResultView {

    long getId();
    String getQuestion();
    @Value("#{target.getResult()}")
    String getResult();
    void setResult(String result);
}
