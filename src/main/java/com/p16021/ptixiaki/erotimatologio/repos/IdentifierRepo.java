package com.p16021.ptixiaki.erotimatologio.repos;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IdentifierRepo extends CrudRepository<Identifier,Long> {

    List<Identifier> findAllByType(IdentifierType responseType);
    List<String> findAllNameByType(ResponseType responseType);

}
