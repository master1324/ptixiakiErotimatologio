package com.p16021.ptixiaki.erotimatologio.models.projections;


import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface QuestionView {

    Long getId();
    String getQuestion();
    //ResponseType getResponseType();
    void setUserResponse(String response);
    //void setEligibleResponses(List<String> eligibleResponses);
    void setResponseId(Long id);
    Long getResponseId();
   // List<String> getEligibleResponses();
    @Value("#{target.getUserResponse()}")
    String getUserResponse();
}
