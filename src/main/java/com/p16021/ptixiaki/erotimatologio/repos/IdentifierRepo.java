package com.p16021.ptixiaki.erotimatologio.repos;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IdentifierRepo extends CrudRepository<Identifier,Long> {

    List<Identifier> findAllByType(IdentifierType responseType);
    @Query(value = "select id,name,type from identifier where type=?1",nativeQuery = true)
    List<Identifier> findQuery(int responseType);
    List<String> findAllNameByType(ResponseType responseType);

}
