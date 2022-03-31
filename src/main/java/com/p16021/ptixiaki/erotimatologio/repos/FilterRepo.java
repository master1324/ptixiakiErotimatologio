package com.p16021.ptixiaki.erotimatologio.repos;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Filter;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FilterRepo extends CrudRepository<Filter,Long> {

    Optional<Filter> findByFilter(String filter);
    boolean existsByFilterAndQuestionnaireId(String filter,long qid);
    Optional<Filter> findByFilterAndQuestionnaireId(String filter,long qid);
    int countByFilterAndQuestionnaireId(String filter,long qid);
}
