package com.p16021.ptixiaki.erotimatologio.models.projections.result;

import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public interface QuestionResultView {

    long getId();
    String getQuestion();
    @Value("#{target.getResult()}")
    String getResult();
    void setResult(String result);
    void setResultMap(Map<String,Integer> resultMap);
    Map<String,Integer> getResultMap();

}
