package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;

import java.util.Map;

public interface QuestionService {
    String findQuestionResult(long qid, String responseType, String filter);
    Map<String,Integer> findResultMap(long questionId,String filter,String responseType);
    ResponseType findQuestionResponseType(long qid);
}
