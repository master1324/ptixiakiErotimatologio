package com.p16021.ptixiaki.erotimatologio.repos;


import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Response;
import com.p16021.ptixiaki.erotimatologio.models.projections.ResponseView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ResponseRepo extends CrudRepository<Response,Long> {

    boolean existsByIdAndUserId(Long id,Long userId);
    ResponseView findProjectedBy();
    List<ResponseView> findAllProjectedByUserIdAndFilterAndQuestionIdIn(long id,String filter,Set<Long> qIds);
    List<ResponseView> findAllByUserId(long id);
    ResponseView findProjectedById(long id);
    List<ResponseView> findAllByQuestionIdAndFilter(long id, String filter);

    @Query(value = "SELECT question.id from question " +
            "inner join question_group on question.question_group_id = question_group.id " +
            "INNER JOIN questionnaire on questionnaire.id = question_group.questionnaire_id where questionnaire.id =?1",nativeQuery = true)
    List<Long> findQuestionsOfQuestionnaire(long qid);

    @Query(value = "SELECT questionnaire.id from questionnaire " +
            "INNER join question_group on questionnaire.id = question_group.questionnaire_id " +
            "INNER JOIN question on question.question_group_id = question_group.id " +
            "INNER JOIN response on response.question_id = question.id where response.id =?1",nativeQuery = true)
    long findQuestionnaireByResponseId(long responseId);

    @Query(value = "select response from response " +
            "where filter=?1 and question_id =?2 " ,nativeQuery = true)
    List<String> getResultMap(String filter, long questionId);

    int countByFilterAndQuestionIdIn(String filter,Iterable<Integer> ids);
}
