package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Teacher;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.TeacherRequest;
import com.p16021.ptixiaki.erotimatologio.models.projections.TeacherProjectionNoUserId;

import java.util.Map;

public interface TeacherService {
    Iterable<TeacherProjectionNoUserId> getTeachers();

    Teacher getTeacher(long id);

    void addTeacher(TeacherRequest teacher);

    void updateTeacher(Teacher teacher,long userId);

    void deleteTeacher(long id);
}
