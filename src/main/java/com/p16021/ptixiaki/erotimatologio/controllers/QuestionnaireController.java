package com.p16021.ptixiaki.erotimatologio.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireIdentifiers;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireBody;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import com.p16021.ptixiaki.erotimatologio.models.projections.result.QuestionnaireResult;

import com.p16021.ptixiaki.erotimatologio.services.abstactions.FilterService;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.QuestionnaireService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/quest")
@RequiredArgsConstructor
@Slf4j
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;
    private final ObjectMapper mapper ;

    @GetMapping("/{qid}")
    public  ResponseEntity<QuestionnaireView> getById(@PathVariable("qid") int id, @RequestParam String filter)  {
        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            if(filter.equals("")){
                QuestionnaireIdentifiers questionnaireIdentifiers = questionnaireService.findById(id,userId);
                return ResponseEntity.ok().body(questionnaireIdentifiers);

            }else{

                QuestionnaireBody questionnaireBody = questionnaireService.findByIdWhereUser(id,userId,filter);
                return ResponseEntity.ok().body(questionnaireBody);

            }
        }catch (RuntimeException e){
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage());
        }

    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<QuestionnaireView>> getAll(){

        Iterable<QuestionnaireView> questionnaireViews = questionnaireService.findAll();

        return ResponseEntity.ok().body(questionnaireViews);
    }

    @GetMapping("/{qid}/results")
    public ResponseEntity<QuestionnaireResult> getResults(@PathVariable long qid, @RequestParam String filter){

        try {
            QuestionnaireResult q = questionnaireService.findResult(qid,filter);
            return ResponseEntity.ok().body(q);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping(value = { "/add" }, consumes= {"application/json"})
    public ResponseEntity<QuestionnaireBody> createQuest(@RequestBody Questionnaire questionnaire){

       questionnaireService.save(questionnaire);
       URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("quest/add").toUriString());
       return ResponseEntity.created(uri).build();

   }

    @DeleteMapping("/{qid}")
    public void deleteQuest(@PathVariable long qid){
      //questionnaireService.deleteById(qid);
   }

    private void throwError(String message, int status, HttpServletResponse response) throws IOException {

        response.setStatus(status);
        Map<String, Object> data = new HashMap<>();

        data.put(
                "exception",
                message);

        response.getOutputStream()
                .println(mapper.writeValueAsString(data));
    }



}
