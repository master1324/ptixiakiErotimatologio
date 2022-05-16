package com.p16021.ptixiaki.erotimatologio.services;


import com.p16021.ptixiaki.erotimatologio.listeners.OnResponseSaved;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.QuestionnaireResponse;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Response;
import com.p16021.ptixiaki.erotimatologio.models.projections.ResponseView;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireBody;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionnaireRepo;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionnaireResponseRepo;
import com.p16021.ptixiaki.erotimatologio.repos.ResponseRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.FilterService;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service @RequiredArgsConstructor @Slf4j
public class ResponseServiceImpl implements  ResponseService {

    private final ResponseRepo responseRepo;
    private final QuestionnaireRepo questionnaireRepo;
    private final QuestionnaireResponseRepo questionnaireResponseRepo;
    private final ResponseValidator responseValidator;
    private final FilterService filterService;


    @Override
    public ResponseView getResponse(long id) {
        return responseRepo.findProjectedById(id);
    }

    @Override
    public List<ResponseView> findAllUserResponsesByFilter(QuestionnaireBody q, Long userId, Long qid, String filter) {

        Set<Long> qIds = new HashSet<>();

        q.getQuestionnaire()
                .forEach(group-> group.getQuestions()
                    .forEach(question->qIds.add(question.getId())));


        return responseRepo.findAllProjectedByUserIdAndFilterAndQuestionIdIn(userId,filter,qIds);
    }

    @Override
    public Iterable<QuestionnaireResponse> findAllQuestResponsesByUser(long userId){
        Iterable<QuestionnaireResponse> qResponses = questionnaireResponseRepo.findByUserId(userId);

        qResponses.forEach(qResponse -> qResponse.setDecodedFilter(filterService.decodeFilter(qResponse.getFilter())));

        return qResponses;
    }

    @Override
    public void save(Response response, Long userId) {

        //if(responseIsOk(response)){
            response.setUserId(userId);
            responseRepo.save(response);
        //}else
            //throw new RuntimeException("response is not eligible");
    }

    @Override
    @Transactional
    public void saveAll(Iterable<Response> responses, long userId,@Nullable OnResponseSaved listener){

        Response rx = responses.iterator().next();
        QuestionnaireView qv = questionnaireRepo.findQuestionnaireByQuestionId(rx.getQuestion().getId());
        if(rx.getId() ==0){
            Optional<QuestionnaireResponse> questionnaireResponseOptional =
                    questionnaireResponseRepo.findByFilterAndQuestionnaireIdAndUserId(rx.getFilter(),qv.getId(),userId);
            if(questionnaireResponseOptional.isPresent()){
                throw new RuntimeException("Παρακαλώ ανανεώστε την σελίδα για να μπορέσετε να αλλάξετε τις απαντήσεις σας");
            }
        }
        //QuestionnaireView qv = questionnaireRepo.findQuestionnaireByQuestionId(rx.getQuestion().getId());

        responseValidator.responsesAreOk(responses,userId,qv.getId());

        responseRepo.saveAll(responses);
        saveQuestionnaireResponse(rx.getFilter(),userId,qv.getId(),qv.getName());
        listener.savedSuccessfully(rx.getFilter(),qv.getId());

    }


    //TODO: CHeck if creating new obj every time is nessesary
    private void saveQuestionnaireResponse(String filter,Long userId,Long questionnaireId,String name){
        Optional<QuestionnaireResponse> optQr = questionnaireResponseRepo.
                findByFilterAndQuestionnaireIdAndUserId(filter,questionnaireId,userId);

        QuestionnaireResponse qr = new QuestionnaireResponse(questionnaireId,userId,filter,name);

        optQr.ifPresent(questionnaireResponse -> qr.setId(questionnaireResponse.getId()));
        questionnaireResponseRepo.save(qr);
    }

    @Override
    public void deleteById(long rid) {
    }


}
