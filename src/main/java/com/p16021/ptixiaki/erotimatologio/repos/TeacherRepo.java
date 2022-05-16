package com.p16021.ptixiaki.erotimatologio.repos;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Teacher;
import com.p16021.ptixiaki.erotimatologio.models.projections.TeacherProjection;
import com.p16021.ptixiaki.erotimatologio.models.projections.TeacherProjectionNoUserId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepo extends CrudRepository<Teacher,Long> {
    boolean existsByAppUserId(long id);
    Optional<Teacher> findByAppUserId(long id);
    List<TeacherProjection>  findBy();
    @NotNull Iterable<TeacherProjectionNoUserId> findProjectedBy();
}
