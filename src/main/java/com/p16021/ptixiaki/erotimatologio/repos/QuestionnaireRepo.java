package com.p16021.ptixiaki.erotimatologio.repos;


import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import org.springframework.data.repository.CrudRepository;

public interface QuestionnaireRepo extends CrudRepository<Questionnaire,Long> {

    boolean existsById(Long id);
    Iterable<QuestionnaireView> findProjectedBy();
    <T> T findProjectedById(long id,Class<T> className);
    Iterable<QuestionnaireView> findByQuestionnaireQuestionsResponsesQuestionId(long id);



}
