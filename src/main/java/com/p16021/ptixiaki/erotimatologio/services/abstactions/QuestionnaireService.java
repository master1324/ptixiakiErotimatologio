package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireBody;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireIdentifiers;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import com.p16021.ptixiaki.erotimatologio.models.projections.result.QuestionnaireResult;

public interface  QuestionnaireService {
    Iterable<QuestionnaireView> findAll();

    QuestionnaireIdentifiers findById(long id);

    QuestionnaireBody findByIdWhereUser(long qid, long userId, String filter);

    QuestionnaireResult findResult(long id, String filter);

    void save(Questionnaire q);
}
