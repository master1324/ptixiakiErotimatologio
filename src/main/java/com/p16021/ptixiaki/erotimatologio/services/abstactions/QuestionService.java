package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;

public interface QuestionService {
    String findQuestionResult(long qid, String responseType, String filter);

    ResponseType findQuestionResponseType(long qid);
}
