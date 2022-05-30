package com.p16021.ptixiaki.erotimatologio.v2.controllers;

import com.p16021.ptixiaki.erotimatologio.listeners.OnResponseSaved;
import com.p16021.ptixiaki.erotimatologio.models.AppResponse;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.QuestionnaireResponse;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Response;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.RegistrationRequest;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.UpdateProfileRequest;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v2/response")
@RequiredArgsConstructor
@Slf4j
public class ResponseControllerV2 {

    private final ResponseService responseService;

    @GetMapping("/all")
    public ResponseEntity<AppResponse> getResponses(){

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        try {
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(generateMap("qresponses" , responseService.findAllQuestResponsesByUser(userId)))
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }catch (Exception e){
            return error(401 ,null,null,null);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse> getResponse(@PathVariable("id") long id){

        return error(501 ,"not implemented","not implemented",null);
    }

    @GetMapping("/q_responses")
    public ResponseEntity<Iterable<QuestionnaireResponse>> getAllQuestionnaireResponses(){

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        Iterable<QuestionnaireResponse> responses = responseService.findAllQuestResponsesByUser(userId);
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/add")
    public ResponseEntity<AppResponse> addResponse(@RequestBody RegistrationRequest request){

        return error(501 ,"not implemented","not implemented",null);
    }

    @PostMapping("/addAll")
    public ResponseEntity<AppResponse> addAllResponses(@RequestBody Iterable<Response> responses){

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        try {
            responseService.saveAll(responses, userId, new OnResponseSaved() {
                @Override
                public void savedSuccessfully(String filter, long questionnaireId) {

                }
            });
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(CREATED)
                            .statusCode(CREATED.value())
                            .build()
            );
        }catch (RuntimeException runtimeException){
            return error(401 ,null,null,runtimeException.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
            return error(401 ,null,null,e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AppResponse> updateResponse(@RequestBody UpdateProfileRequest request){

        return error(501 ,"not implemented","not implemented",null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppResponse> deleteResponse(@PathVariable long id){

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

    private <K,V> HashMap<K,V> generateMap(K key, V value){
        return new HashMap<K,V>(){{
            put(key, value);
        }};
    }
}
