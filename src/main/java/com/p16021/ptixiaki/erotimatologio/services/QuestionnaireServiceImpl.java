package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestGroupView;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestionView;
import com.p16021.ptixiaki.erotimatologio.models.projections.ResponseView;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireBody;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireIdentifiers;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import com.p16021.ptixiaki.erotimatologio.models.projections.result.QuestGroupResultView;
import com.p16021.ptixiaki.erotimatologio.models.projections.result.QuestionResultView;
import com.p16021.ptixiaki.erotimatologio.models.projections.result.QuestionnaireResult;
import com.p16021.ptixiaki.erotimatologio.repos.FilterRepo;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionnaireRepo;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionnaireResponseRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType.TEXT;

@Service @RequiredArgsConstructor @Slf4j
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final QuestionnaireRepo questionnaireRepo;
    private final FilterRepo filterRepo;
    private final ResponseService responseService;
    private final QuestionService questionService;
    private final IdentifierService identifierService;
    private final QuestionnaireResponseRepo questionnaireResponseRepo;

    @Override
    public Iterable<QuestionnaireView> findAll(Set<String> roles){

        if(roles.contains("ROLE_ADMIN")){
            return questionnaireRepo.findProjectedBy();
        }else{
            return questionnaireRepo.findProjectedByEnabled(true);
        }

    }

    @Override
    public QuestionnaireBody findById(long id) {

        return questionnaireRepo.findProjectedById(id,QuestionnaireBody.class);
    }

    @Override
    public QuestionnaireIdentifiers findById(long id,long userId){

        QuestionnaireIdentifiers questionnaire = questionnaireRepo.findProjectedById(id,QuestionnaireIdentifiers.class);

        if (questionnaire != null){
            Map<IdentifierType,List<Identifier>> map = new HashMap<>();
            questionnaire.getIdentifiers().forEach(i -> map.put(i,identifierService.findByIdentifierType(i,userId)));
            questionnaire.setEligibleResponsesIdentifiers(map);

            return questionnaire;
        }else{
            throw new RuntimeException("Den uparxei to erotimalogio me id " + id);
        }

    }

    @Override
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
                            //question.setEligibleResponses(eligibleResponses);
                        }
                    }
                }

                return questionnaire;
            }else{
                throw new RuntimeException("Den uparxei to erotimalogio me id " + qid);
            }



    }

    @Override
    public QuestionnaireResult findResult(long id, String filter){

        QuestionnaireResult questionnaireResultView = questionnaireRepo.findProjectedById(id, QuestionnaireResult.class);

        if(questionnaireResultView != null){
            for(QuestGroupResultView group: questionnaireResultView.getQuestionnaire()){
                for(QuestionResultView question: group.getQuestions()){
                    String result = questionService.findQuestionResult(question.getId(),group.getResponseType(),filter);
                    question.setResultMap(questionService.findResultMap(question.getId(),filter,group.getResponseType()));
                    //TODO: to result tha ginete set mono gia text
                    question.setResult(result);
                }
            }
            return questionnaireResultView;
        }else {
            throw new RuntimeException("Den uparxei to erotimalogio me id " + id);
        }

    }

    @Override
    public void save(Questionnaire q){
        q.setEnabled(true);
        questionnaireRepo.save(q);

    }

    @Override
    @Transactional
    public void update(Questionnaire q) {
        questionnaireRepo.save(q);
        filterRepo.updateFilterWhereQuestionnaireId(q.getId(),q.getName());
    }

    @Override
    @Transactional
    public void delete(long id) {
        questionnaireRepo.deleteById(id);
        questionnaireResponseRepo.deleteByQuestionnaireId(id);
        filterRepo.deleteByQuestionnaireId(id);
    }


}
