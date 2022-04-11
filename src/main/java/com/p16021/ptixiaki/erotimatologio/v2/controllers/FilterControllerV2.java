package com.p16021.ptixiaki.erotimatologio.v2.controllers;

import com.p16021.ptixiaki.erotimatologio.models.AppResponse;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Filter;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.RegistrationRequest;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.UpdateProfileRequest;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.FilterService;
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
@RequestMapping("/v2/filter")
@RequiredArgsConstructor
@Slf4j
public class FilterControllerV2 {

    private final FilterService filterService;

    @GetMapping("/all")
    public ResponseEntity<AppResponse> getFilters(){

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        try {
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("filters" , filterService.getAllFilters(userId)))
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }catch (Exception e){
            log.error(e.getMessage());
            return error(401 ,null,null,null);
        }

    }

    @GetMapping("/{filter}")
    public ResponseEntity<AppResponse> getFilter(@PathVariable("filter") String filter){

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        try{
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("user" , filterService.getFilter(userId,filter)))
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }catch (Exception e){
            return error(404 ,null,null,"not found sss");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<AppResponse> addFilter(@RequestBody Filter filter){

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        try {
            filterService.saveFilter(filter,userId);
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            //.data(Map.of("teacher" , teacherService.addTeacher(teacher)))
                            .status(CREATED)
                            .statusCode(CREATED.value())
                            .build()
            );
        }catch (Exception e){
            return error(401 ,"tell","me","why");
        }
    }

    @PutMapping("/update/")
    public ResponseEntity<AppResponse> updateFilter(@RequestBody Filter filter){

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        try{
            filterService.updateFilter(filter,userId);
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
    public ResponseEntity<AppResponse> deleteUser(@PathVariable long id){

//        try{
//            teacherService.deleteTeacher(id);
//            return ResponseEntity.ok(
//                    AppResponse.builder()
//                            .timeStamp(LocalDateTime.now())
//                            //.data(Map.of("teacher" , teacherService.deleteTeacher(id)))
//                            .status(OK)
//                            .statusCode(OK.value())
//                            .build()
//            );
//        }catch (Exception e){
//            return error(401 ,null,null,null);
//        }

        return error(501 ,"not implemented","not implemented",null);

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
