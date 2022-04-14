package com.p16021.ptixiaki.erotimatologio.repos;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.QuestionnaireResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuestionnaireResponseRepo extends CrudRepository<QuestionnaireResponse,Long> {

    Optional<QuestionnaireResponse> findByFilterAndQuestionnaireIdAndUserId(String filter, long qid, long userId);

    Iterable<QuestionnaireResponse> findByUserId(long userId);

    void deleteByQuestionnaireId(long id);
}
