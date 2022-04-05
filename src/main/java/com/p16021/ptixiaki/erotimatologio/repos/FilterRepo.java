package com.p16021.ptixiaki.erotimatologio.repos;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Filter;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FilterRepo extends CrudRepository<Filter,Long> {

    Optional<Filter> findByFilter(String filter);
    boolean existsByFilterAndQuestionnaireId(String filter,long qid);
    boolean existsByFilterAndQuestionnaireIdAndEnabled(String filter,long qid,boolean enabled);
    Optional<Filter> findByFilterAndQuestionnaireId(String filter,long qid);
    int countByFilterAndQuestionnaireId(String filter,long qid);

    @Modifying
    @Query("update Filter f set f.enabled = false where f.activeFor < :date")
    void disableFilters(@Param("date") long date);
}
