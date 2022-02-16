package com.p16021.ptixiaki.erotimatologio.repos;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.QuestionGroup;
import com.p16021.ptixiaki.erotimatologio.models.projections.QuestGroupView;
import org.springframework.data.repository.CrudRepository;

public interface QuestionGroupRepo extends CrudRepository<QuestionGroup,Long> {

    QuestionGroup findByQuestionsResponsesUserId(long id);
    QuestGroupView findByQuestionsId(long id);

}
