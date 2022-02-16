package com.p16021.ptixiaki.erotimatologio.repos;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Question;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestionView;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepo extends CrudRepository<Question,Long> {

    List<QuestionView> findProjectedByResponsesUserId(long userId);
    Optional<QuestionView> findById(long qid);
}
