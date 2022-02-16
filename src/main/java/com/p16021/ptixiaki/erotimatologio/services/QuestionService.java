package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Question;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.QuestionGroup;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestGroupView;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestionView;
import com.p16021.ptixiaki.erotimatologio.models.projections.ResponseView;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionGroupRepo;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionRepo;
import com.p16021.ptixiaki.erotimatologio.repos.ResponseRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BinaryOperator;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final ResponseRepo responseRepo;
    private final QuestionRepo questionRepo;
    private final QuestionGroupRepo questionGroupRepo;

    public String findQuestionResult(long qid,String responseType,String filter){

        Iterable<ResponseView> responses = responseRepo.findAllByQuestionIdAndFilter(qid,filter);
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

    public ResponseType findQuestionResponseType(long qid) {
        Optional<QuestionView> question = questionRepo.findById(qid);
        if (question.isPresent()){
            QuestGroupView group = questionGroupRepo.findByQuestionsId(qid);
            return group.getResponseType();
        }
        return null;
    }
}
