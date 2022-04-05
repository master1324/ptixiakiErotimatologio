package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Teacher;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.AppUser;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.RegistrationRequest;
import com.p16021.ptixiaki.erotimatologio.repos.TeacherRepo;
import com.p16021.ptixiaki.erotimatologio.repos.UserRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceI implements TeacherService {

    private final PasswordEncoder passwordEncoder;
    private final TeacherRepo teacherRepo;
    private final UserRepo userRepo;

    @Override
    public Iterable<Teacher> getTeachers() {
        return teacherRepo.findAll();
    }

    @Override
    public Teacher getTeacher(long id) {

        Optional<Teacher> teacherOptional = teacherRepo.findById(id);

        if (teacherOptional.isPresent()){
            return teacherOptional.get();
        }else {
            throw new RuntimeException("Teacher not found");
        }

    }

    @Override
    @Transactional
    public void addTeacher(Map<String,Object> teacher) {

        Teacher teacher1 = (Teacher) teacher.get("teacher");
        RegistrationRequest request = (RegistrationRequest) teacher.get("request");

        boolean userExists = userRepo.findByEmail(request.getEmail()).isPresent();
        if(userExists){
            throw new IllegalStateException("email taken");
        }

        teacherRepo.save(teacher1);
        userRepo.save(new AppUser(request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                true));

    }

    @Override
    public void updateTeacher(Teacher teacher,long userID) {

        //AN O XRISTIS DEN EINAI ADMIN MIN AFISEIS TEACHER NA KANEI UPDATE ALLON TEACHER
       if(userID != -1 ){
            if(teacher.getAppUserId() != userID){
                throw new RuntimeException("Den uparxei o teacher");
            }
       }
        if(teacherRepo.findById(teacher.getId()).isPresent()){
            teacherRepo.save(teacher);
        }
    }

    @Override
    public void deleteTeacher(long id) {
        teacherRepo.deleteById(id);
    }
}
