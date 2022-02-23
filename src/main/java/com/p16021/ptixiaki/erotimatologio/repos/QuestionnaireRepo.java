package com.p16021.ptixiaki.erotimatologio.repos;


import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireValidators;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuestionnaireRepo extends CrudRepository<Questionnaire,Long> {

    boolean existsById(Long id);
    Iterable<QuestionnaireView> findProjectedBy();
    <T> T findProjectedById(long id,Class<T> className);
    Iterable<QuestionnaireView> findByQuestionnaireQuestionsResponsesQuestionId(long id);

    @Query(value = "SELECT questionnaire.id,questionnaire.name from questionnaire " +
            "INNER join question_group on questionnaire.id = question_group.questionnaire_id " +
            "INNER JOIN question on question.question_group_id = question_group.id " +
            "INNER JOIN response on response.question_id = question.id where response.id =?1",nativeQuery = true)
    Optional<QuestionnaireView> findQuestionnaireByResponseId(long responseId);


}
