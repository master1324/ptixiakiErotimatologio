package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.QuestionnaireResponse;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Response;
import com.p16021.ptixiaki.erotimatologio.models.projections.ResponseView;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireBody;

import java.util.List;

public interface ResponseService {
    ResponseView getResponse(long id);

    List<ResponseView> findAllUserResponsesByFilter(QuestionnaireBody q, Long userId, Long qid, String filter);

    Iterable<QuestionnaireResponse> findAllQuestResponsesByUser(long userId);

    void save(Response response, Long userId);

    void saveAll(Iterable<Response> responses, long userId);

    void deleteById(long rid);
}
