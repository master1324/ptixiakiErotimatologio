package com.p16021.ptixiaki.erotimatologio.repos;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Teacher;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TeacherRepo extends CrudRepository<Teacher,Long> {
    boolean existsByAppUserId(long id);
    Optional<Teacher> findByAppUserId(long id);
}
