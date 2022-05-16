package com.p16021.ptixiaki.erotimatologio.v2.controllers;

import com.p16021.ptixiaki.erotimatologio.models.AppResponse;
import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Teacher;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.TeacherRequest;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v2/teacher")
@RequiredArgsConstructor
@Slf4j
public class TeacherControllerV2 {

    private final TeacherService teacherService;

    @GetMapping("/all")
    public ResponseEntity<AppResponse> getTeachers(){

        try {
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("teachers" , teacherService.getTeachers()))
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }catch (Exception e){
            return error(401 ,null,null,null);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse> getTeacher(@PathVariable("id") long id){

        try{
            return
            ResponseEntity.ok(
             AppResponse.builder()
              .timeStamp(LocalDateTime.now())
              .data(Map.of("teacher" , teacherService.getTeacher(id)))
              .status(OK)
              .statusCode(OK.value())
              .build()
            );
        }catch (Exception e){
            return error(401 ,null,null,null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<AppResponse> addTeacher(@RequestBody TeacherRequest teacher){
        try {
            teacherService.addTeacher(teacher);
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            //.data(Map.of("teacher" , teacherService.addTeacher(teacher)))
                            .status(CREATED)
                            .statusCode(CREATED.value())
                            .build()
            );
        }catch (Exception e){
            log.error(e.toString());
            return error(401 ,"tell","me","why");
        }
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<AppResponse> updateTeacher(@RequestBody Teacher teacher){

        long userId = -1;

        if (SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TEACHER"))) {
             userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        }

        try{
            teacherService.updateTeacher(teacher,userId);
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            //.data(Map.of("teacher" , teacherService.updateTeacher(teacher)))
                            .status(CREATED)
                            .statusCode(CREATED.value())
                            .build()
            );
        }catch (Exception e){
            return error(401 ,null,null,null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppResponse> deleteTeacher(@PathVariable long id){

        try{
            teacherService.deleteTeacher(id);
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            //.data(Map.of("teacher" , teacherService.deleteTeacher(id)))
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }catch (Exception e){
            return error(401 ,null,null,null);
        }


    }

    private ResponseEntity<AppResponse> error(int status, @Nullable String developerMessage, @Nullable String reason, @Nullable String message){

        AppResponse errorResponse = AppResponse.builder()
                .timeStamp(LocalDateTime.now())
                .reason(reason)
                .developerMessage(developerMessage)
                .message(message)
                .status(HttpStatus.valueOf(status))
                .statusCode(status)
                .build();

        return new ResponseEntity<>(errorResponse,HttpStatus.valueOf(status));

    }

}
