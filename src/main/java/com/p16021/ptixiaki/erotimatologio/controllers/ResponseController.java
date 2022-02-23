package com.p16021.ptixiaki.erotimatologio.controllers;



import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.QuestionnaireResponse;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Response;

import com.p16021.ptixiaki.erotimatologio.models.projections.ResponseView;
import com.p16021.ptixiaki.erotimatologio.repos.ResponseRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.QuestionnaireService;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/response")
@RequiredArgsConstructor
@Slf4j
public class ResponseController {

    private final ResponseService responseService;
    private final ResponseRepo responseRepo;
    private final QuestionnaireService questionnaireService;

    @GetMapping("/{rid}")
    public ResponseEntity<ResponseView> getResponse(@PathVariable long rid){

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        ResponseView responseView = responseService.getResponse(rid);

        return ResponseEntity.ok().body(responseView);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseView>> getAllUserResponses(@RequestParam long questionnaireId){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        //List<ResponseView> responseViews = responseService.findAll(userId,questionnaireId);
        List<ResponseView> responseViews = responseRepo.findAllByUserId(1L);

        return ResponseEntity.ok().body(responseViews);
    }

    @GetMapping("/all_quest_responses")
    public ResponseEntity<Iterable<QuestionnaireResponse>> getAllQuestionnaireResponses(){

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        Iterable<QuestionnaireResponse> responses = responseService.findAllQuestResponsesByUser(userId);
        return ResponseEntity.ok().body(responses);
    }


    @PostMapping(value = { "/add" }, consumes= {"application/json"})
    public ResponseEntity<Response> sendResponse(@RequestBody Response response){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        responseService.save(response,userId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("response/add").toUriString());
        return ResponseEntity.created(uri).build();
    }

    @PostMapping(value = { "/addAll" }, consumes= {"application/json"})
    public ResponseEntity<Response> sendResponses(@RequestBody Iterable<Response> response) {
        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        responseService.saveAll(response,userId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("response/add").toUriString());
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{rid}")
    public ResponseEntity<Response> updateResponse(@PathVariable long rid, @RequestBody Response response){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        responseService.save(response,userId);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{rid}")
    public ResponseEntity<Response> deleteResponse(@PathVariable long rid, long qid){
        responseService.deleteById(rid);
        return  ResponseEntity.ok().build();
    }



}
