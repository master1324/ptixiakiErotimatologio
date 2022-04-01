package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Response;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.FilterService;
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
    private final FilterService filterService;

    public boolean responseIsOk(Response response){
        long qid = response.getQuestion().getId();
        ResponseType responseType = questionService.findQuestionResponseType(qid);
        if(responseType!=null){
            if(!responseType.equals(ResponseType.TEXT)) {
                List<String> eligibleResponses = identifierService.findEligibleResponses(responseType);
                boolean containsAcceptableResponse = eligibleResponses.stream().anyMatch(s -> s.equals(response.getResponse()));
                if(containsAcceptableResponse){
                    return true;
                }else{
                    throw new RuntimeException("I apantisi den simbadizei me tis dektes apantiseis");
                }
            }else{
                // PERIPTOSI TEXT
                return true;
            }
        }else{
            throw new RuntimeException("Oi apantiseis den einai ok");
        }
    }

    public boolean responsesAreOk(Iterable<Response> responses,Long userID,Long questionnaireID){

        for (Response response:responses){
            responseIsOk(response);
            boolean filterIsOk = filterService.filterIsOk(response.getFilter(),questionnaireID);
            response.setUserId(userID);
            if(!responseIsOk(response) ){
                throw new RuntimeException("Μη αποδεκτή απάντηση");
            }
            if(!filterIsOk){
                throw new RuntimeException("Τα αναγνωριστικά που επιλέξατε δεν δέχονται απαντήσεις αυτήν την στιγμή");
            }
        }

        //TODO: AN OLA TA FILTER EINAI IDIA
        //TODO: AN OI APANTISEIS ANOIKOUN STON IDIO EROTIMATOLOGIO

        return true;
    }

}
