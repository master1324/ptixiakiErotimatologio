package com.p16021.ptixiaki.erotimatologio.v2.controllers;

import com.p16021.ptixiaki.erotimatologio.models.AppResponse;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.RegistrationRequest;
import com.p16021.ptixiaki.erotimatologio.models.entities.user.UpdateProfileRequest;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.QuestionnaireService;
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
@RequestMapping("/v2/quest")
@RequiredArgsConstructor
@Slf4j
public class QuestionnaireControllerV2 {

    private final QuestionnaireService questionnaireService;

    @GetMapping("/all")
    public ResponseEntity<AppResponse> getQuestionnaires(){
        try {
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("questionnaires" , questionnaireService.findAll()))
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }catch (Exception e){
            return error(401 ,null,null,null);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse> getQuestionnaire(@PathVariable("id") int id, @RequestParam String filter){

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        try{

            if(filter.equals("")){
                return ResponseEntity.ok(
                        AppResponse.builder()
                                .timeStamp(LocalDateTime.now())
                                .data(Map.of("questionnaire" , questionnaireService.findById(id,userId)))
                                .status(OK)
                                .statusCode(OK.value())
                                .build()
                );
            }else if( filter.equals("info")) {
                return ResponseEntity.ok(
                        AppResponse.builder()
                                .timeStamp(LocalDateTime.now())
                                .data(Map.of("questionnaire" , questionnaireService.findById(id)))
                                .status(OK)
                                .statusCode(OK.value())
                                .build()
                );
            }else {
                return ResponseEntity.ok(
                        AppResponse.builder()
                                .timeStamp(LocalDateTime.now())
                                .data(Map.of("questionnaire" , questionnaireService.findByIdWhereUser(id,userId,filter)))
                                .status(OK)
                                .statusCode(OK.value())
                                .build()
                );
            }

        }catch (Exception e){
            return error(404 ,null,null,"not found sss");
        }
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<AppResponse> getQuestionnaireResults(@PathVariable long id, @RequestParam String filter){
        try {
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("results" , questionnaireService.findResult(id,filter)))
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }catch (Exception e){
            log.error(e.toString());
            return error(404 ,null,null,null);
        }

    }

    @PostMapping("/add")
    public ResponseEntity<AppResponse> addQuestionnaire(@RequestBody Questionnaire questionnaire){
        try {
            questionnaireService.save(questionnaire);
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(CREATED)
                            .statusCode(CREATED.value())
                            .build()
            );
        }catch (Exception e){
            return error(401 ,"tell","me","why");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<AppResponse> updateQuestionnaire(@RequestBody Questionnaire questionnaire){

        try {
            questionnaireService.update(questionnaire);
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(CREATED)
                            .statusCode(CREATED.value())
                            .build()
            );
        }catch (Exception e){
            log.error(e.toString());
            return error(401 ,"tell","me","why");
        }

        //return error(501 ,"not implemented","not implemented",null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppResponse> deleteQuestionnaire(@PathVariable long id){

        try {
            questionnaireService.delete(id);
            return ResponseEntity.ok(
                    AppResponse.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }catch (Exception e){
            log.error(e.toString());
            return error(401 ,"tell","me","why");
        }

        //return error(501 ,"not implemented","not implemented",null);

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
