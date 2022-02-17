package com.p16021.ptixiaki.erotimatologio.services;


import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.QuestionnaireResponse;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Response;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import com.p16021.ptixiaki.erotimatologio.models.projections.ResponseView;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireBody;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionnaireResponseRepo;
import com.p16021.ptixiaki.erotimatologio.repos.ResponseRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

import static com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType.YEAR;

@Service @RequiredArgsConstructor @Slf4j
public class ResponseService {

    private final ResponseRepo responseRepo;
    private final QuestionnaireResponseRepo questionnaireResponseRepo;
    private final ResponseValidator responseValidator;


    public ResponseView getResponse(long id) {
        return responseRepo.findProjectedById(id);
    }

    public List<ResponseView> findAllUserResponsesByFilter(QuestionnaireBody q, Long userId, Long qid, String filter) {

        Set<Long> qIds = new HashSet<>();

        q.getQuestionnaire()
                .forEach(group-> group.getQuestions()
                    .forEach(question->qIds.add(question.getId())));


        return responseRepo.findAllProjectedByUserIdAndFilterAndQuestionIdIn(userId,filter,qIds);
    }

    public Iterable<QuestionnaireResponse> findAllQuestResponsesByUser(long userId){
        Iterable<QuestionnaireResponse> qResponses = questionnaireResponseRepo.findByUserId(userId);

        qResponses.forEach(qresponse -> qresponse.setDecodedFilter(decodeFilter(qresponse.getFilter())));

        return questionnaireResponseRepo.findByUserId(userId);
    }

    public void save(Response response, Long userId) {

        //if(responseIsOk(response)){
            response.setUserId(userId);
            responseRepo.save(response);
        //}else
            //throw new RuntimeException("response is not eligible");
    }

    public void saveAll(Iterable<Response> responses,long userId){

        if(responses !=null) {
            Response rx = responses.iterator().next();
            for (Response r : responses) {
                boolean isOk = responseValidator.responseIsOk(r);
                if (!isOk) {
                    throw new RuntimeException("I apantisi " + r.getResponse() + " me id " + r.getId() + " den einai apodekti");
                }
                r.setUserId(userId);
            }
            responseRepo.saveAll(responses);
            long qid = responseRepo.findQuestionnaireByResponseId(rx.getId());
            saveQuestionnaireResponse(rx.getFilter(),userId,qid);
        }else{
            throw new RuntimeException("Den uparxoun apantises");
        }
    }

    //TODO: CHeck if creating new obj every time is nessesary
    private void saveQuestionnaireResponse(String filter,Long userId,Long questionnaireId){
        Optional<QuestionnaireResponse> optQr = questionnaireResponseRepo.
                findByFilterAndQuestionnaireIdAndUserId(filter,questionnaireId,userId);

        QuestionnaireResponse qr = new QuestionnaireResponse(questionnaireId,userId,filter);

        optQr.ifPresent(questionnaireResponse -> qr.setId(questionnaireResponse.getId()));
        questionnaireResponseRepo.save(qr);
    }

    public void deleteById(long rid) {
    }

    //TODO : CHECK FILTER VALIDITY
//    private boolean responseIsOk(Response response){
//        long qid = response.getQuestion().getId();
//        ResponseType responseType = questionService.findQuestionResponseType(qid);
//        if(responseType!=null){
//            if(!responseType.equals(ResponseType.TEXT)) {
//                List<String> eligibleResponses = identifierService.findEligibleResponses(responseType);
//                return eligibleResponses.stream().anyMatch(s -> s.equals(response.getResponse()));
//            }else{
//                return true;
//            }
//        }else{
//            throw new RuntimeException("Oi apantiseis den einai ok");
//        }
//    }

    private boolean filterIsOk(){

        return true;
    }

    private Map<IdentifierType,String> decodeFilter(String filter){

        Map<IdentifierType,String> decodedFilter = new HashMap<>();

        List<String> items = Arrays.asList(filter.split("\\s*,\\s*"));

        String filterMinusYear = StringUtils.substring(filter, 4);
        decodedFilter.put(YEAR,StringUtils.substring(filter, 0, 3) );

        return null;
    }
}
