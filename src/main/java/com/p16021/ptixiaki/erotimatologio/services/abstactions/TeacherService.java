package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Teacher;

import java.util.Map;

public interface TeacherService {
    Iterable<Teacher> getTeachers();

    Teacher getTeacher(long id);

    void addTeacher(Map<String,Object> teacher);

    void updateTeacher(Teacher teacher,long userId);

    void deleteTeacher(long id);
}
