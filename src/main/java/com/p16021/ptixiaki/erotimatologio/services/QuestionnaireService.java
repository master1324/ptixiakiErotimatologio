package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.QuestionnaireResponse;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestGroupView;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestionView;
import com.p16021.ptixiaki.erotimatologio.models.projections.ResponseView;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireBody;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireIdentifiers;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import com.p16021.ptixiaki.erotimatologio.models.projections.result.QuestGroupResultView;
import com.p16021.ptixiaki.erotimatologio.models.projections.result.QuestionResultView;
import com.p16021.ptixiaki.erotimatologio.models.projections.result.QuestionnaireResult;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionnaireRepo;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionnaireResponseRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @RequiredArgsConstructor @Slf4j
public class QuestionnaireService {

    private final QuestionnaireRepo questionnaireRepo;

    private final ResponseService responseService;
    private final QuestionService questionService;
    private final IdentifierService identifierService;

    public Iterable<QuestionnaireView> findAll(){

        return questionnaireRepo.findProjectedBy();
    }

    public QuestionnaireIdentifiers findById(long id){

        QuestionnaireIdentifiers questionnaire = questionnaireRepo.findProjectedById(id,QuestionnaireIdentifiers.class);

        if (questionnaire != null){
            Map<IdentifierType,List<Identifier>> map = new HashMap<>();
            questionnaire.getIdentifiers().forEach(i -> map.put(i,identifierService.findByIdentifierType(i)));
            questionnaire.setEligibleResponsesIdentifiers(map);

            return questionnaire;
        }else{
            throw new RuntimeException("Den uparxei to erotimalogio me id " + id);
        }

    }

    public QuestionnaireBody findByIdWhereUser(long qid, long userId, String filter){

        QuestionnaireBody questionnaire = questionnaireRepo.findProjectedById(qid, QuestionnaireBody.class);

        if(questionnaire != null){

            List<ResponseView> userResponses = responseService
                    .findAllUserResponsesByFilter(questionnaire,userId,qid,filter);

            if(userResponses != null){
                for(QuestGroupView group:questionnaire.getQuestionnaire()){

                    List<String> eligibleResponses = identifierService.findEligibleResponses(group.getResponseType());
                    group.setEligibleResponses(eligibleResponses);

                    for (QuestionView question: group.getQuestions()){

                        Optional<ResponseView> response = userResponses.stream()
                                .filter(r -> r.getQuestion().getId().equals(question.getId())).findAny();

                        response.ifPresent(responseView -> question.setUserResponse(responseView.getResponse()));
                        response.ifPresent(responseView -> question.setResponseId(responseView.getId()));
                        question.setEligibleResponses(eligibleResponses);
                    }
                }
            }

            return questionnaire;
        }else{
            throw new RuntimeException("Den uparxei to erotimalogio me id " + qid);
        }


    }

    public QuestionnaireResult findResult(long id, String filter){

        QuestionnaireResult questionnaireResultView = questionnaireRepo.findProjectedById(id, QuestionnaireResult.class);

        if(questionnaireResultView != null){
            for(QuestGroupResultView group: questionnaireResultView.getQuestionnaire()){
                for(QuestionResultView question: group.getQuestions()){
                    String result = questionService.findQuestionResult(question.getId(),group.getResponseType(),filter);
                    question.setResult(result);
                }
            }
            return questionnaireResultView;
        }else {
            throw new RuntimeException("Den uparxei to erotimalogio me id " + id);
        }

    }

    public Iterable<QuestionnaireView> findByQuestionId(long qid){
        return questionnaireRepo.findByQuestionnaireQuestionsResponsesQuestionId(qid);
    }

    public void save(Questionnaire q){

        questionnaireRepo.save(q);

    }

    public void deleteById(long qid) {
    }

    private String produceFilter(Set<Identifier> identifiers){
        Set<String> ids = new HashSet<>();
        identifiers.forEach(i->ids.add(String.valueOf(i.getId())));
        return String.join("",ids);
    }


}
