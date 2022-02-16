package com.p16021.ptixiaki.erotimatologio.repos;


import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Response;
import com.p16021.ptixiaki.erotimatologio.models.projections.ResponseView;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ResponseRepo extends CrudRepository<Response,Long> {

    boolean existsByIdAndUserId(Long id,Long userId);
    ResponseView findProjectedBy();
    List<ResponseView> findAllProjectedByUserIdAndFilterAndQuestionIdIn(long id,String filter,Set<Long> qIds);
    List<ResponseView> findAllByUserId(long id);
    ResponseView findProjectedById(long id);
    Iterable<ResponseView> findAllByQuestionIdAndFilter(long id, String filter);
}
