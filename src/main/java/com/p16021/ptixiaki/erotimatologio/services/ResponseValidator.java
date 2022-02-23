package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Response;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.IdentifierService;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
@Slf4j
public class ResponseValidator {

    private final IdentifierService identifierService;
    private final QuestionService questionService;

    public boolean isOk(Response response){
        long qid = response.getQuestion().getId();
        ResponseType responseType = questionService.findQuestionResponseType(qid);
        if(responseType!=null){
            if(!responseType.equals(ResponseType.TEXT)) {
                List<String> eligibleResponses = identifierService.findEligibleResponses(responseType);
                return eligibleResponses.stream().anyMatch(s -> s.equals(response.getResponse()));
            }else{
                return true;
            }
        }else{
            throw new RuntimeException("Oi apantiseis den einai ok");
        }
    }

}
