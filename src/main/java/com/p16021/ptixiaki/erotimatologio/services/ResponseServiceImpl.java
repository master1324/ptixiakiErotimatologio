package com.p16021.ptixiaki.erotimatologio.services;


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
    public void saveAll(Iterable<Response> responses, long userId){

//        if(responses.iterator().hasNext()) {
//            Response rx = responses.iterator().next();
//            String filter = rx.getFilter();
//
//            log.info(String.valueOf(rx.getQuestion().getId()) + "XXXXXXXXXXXXXXXXXXXXXXXXX");
//            QuestionnaireView qv = questionnaireRepo.findQuestionnaireByQuestionId(rx.getQuestion().getId());
//            //long qid = responseRepo.findQuestionnaireByResponseId(rx.getId());
//
//
//            if(qv.isPresent()){
//                List<Long> questionIds = responseRepo.findQuestionsOfQuestionnaire(qv.get().getId());
//                //AN TO FILTER DEN EXEI LIKSEI
//                if (filterService.filterIsOk(filter,qv.get().getId())){
//                    for (Response r : responses) {
//                        //AN I EROTISI YPARXEI SE AUTO TO EPOTIMATOLOGIO
//                        if(questionIds.stream().anyMatch(id-> r.getQuestion().getId().equals(id))){
//                            if(r.getFilter().equals(filter)){
//                                //AN I APANTISI EINAI APODEKTI BASI TON TIPO EROTISIS
//                                if (!responseValidator.responseIsOk(r)){
//                                    throw new RuntimeException("I apantisi " + r.getResponse() + " me id " + r.getId() + " den einai apodekti");
//                                }
//                                r.setUserId(userId);
//                            }else {
//                                throw new RuntimeException("MH APODEKTA FILTER");
//                            }
//                        }else{
//                            throw new RuntimeException("I EROTISI DEN YPAXEI SE AUTO TO EROTIMATOLOGIO");
//                        }
//                        //AN KATHE APANTISEI EXEI TO IDIO FILTER
//                    }
//                }else{
//                    throw new RuntimeException("TO EROTIMATOLOGIO DEN DEXETE PIA APANTISEIS");
//                }
//
//                responseRepo.saveAll(responses);
//
//                saveQuestionnaireResponse(rx.getFilter(),userId,qv.get().getId(),qv.get().getName());
//            }else{
//                throw new RuntimeException("TO EROTIMATOLOGIO DEN YPARXEI");
//            }
//
//        }else{
//            throw new RuntimeException("Den uparxoun apantises");
//        }\

        Response rx = responses.iterator().next();
        log.info(rx.toString());
        QuestionnaireView qv = questionnaireRepo.findQuestionnaireByQuestionId(rx.getQuestion().getId());
        log.info(qv.toString());

        responseValidator.responsesAreOk(responses,userId,qv.getId());
        responseRepo.saveAll(responses);
        saveQuestionnaireResponse(rx.getFilter(),userId,qv.getId(),qv.getName());

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
