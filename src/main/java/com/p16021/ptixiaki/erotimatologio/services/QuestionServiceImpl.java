package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestGroupView;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestionView;
import com.p16021.ptixiaki.erotimatologio.models.projections.ResponseView;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionGroupRepo;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionRepo;
import com.p16021.ptixiaki.erotimatologio.repos.ResponseRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.IdentifierService;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final ResponseRepo responseRepo;
    private final QuestionRepo questionRepo;
    private final QuestionGroupRepo questionGroupRepo;
    private final IdentifierService identifierService;

    @Override
    public String findQuestionResult(long qid, String responseType, String filter){
        //select response,count(response) from response where filter="2022,1,2,5,6,8" and question_id =35 GROUP by response order by count(response) DESC

        List<ResponseView> responses = responseRepo.findAllByQuestionIdAndFilter(qid,filter);
        List<String> responsesText = new ArrayList<>();
        responses.forEach(r->responsesText.add(r.getResponse()));
        if(!responseType.toString().equals("TEXT")){
            return responsesText.stream()
                    .reduce(BinaryOperator.maxBy(Comparator.comparingInt
                            (o -> Collections.frequency(responsesText, o)))).orElse(null);
        }else{
            return String.join(",", responsesText);
        }
    }

    @Override
    public Map<String, Integer> findResultMap(long questionId, String filter, String responseType) {

        Map<String,Integer> results = new LinkedHashMap<>();
        if(!responseType.equals("TEXT")){
            List<String> responseList = responseRepo.getResultMap(filter,questionId);
            List<String> eligibleResponses = identifierService.findEligibleResponses(ResponseType.valueOf(responseType));
            eligibleResponses.forEach(value ->{
                results.put(value,Collections.frequency(responseList,value));
            });
        }
        return results;
    }


    @Override
    public ResponseType findQuestionResponseType(long qid) {
        QuestionView question = questionRepo.findById(qid,QuestionView.class);
        if (question != null){
            QuestGroupView group = questionGroupRepo.findByQuestionsId(qid);
            return group.getResponseType();
        }
        return null;
    }
}
