package com.p16021.ptixiaki.erotimatologio.controllers;


import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireIdentifiers;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireBody;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import com.p16021.ptixiaki.erotimatologio.models.projections.result.QuestionnaireResult;

import com.p16021.ptixiaki.erotimatologio.services.abstactions.QuestionnaireService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;

@RestController
@RequestMapping("/quest")
@RequiredArgsConstructor
@Slf4j
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    @GetMapping("/{qid}")
    public  ResponseEntity<QuestionnaireView> getById(@PathVariable("qid") int id, @RequestParam String filter)  {

        try {
            if(filter.equals("")){
                QuestionnaireIdentifiers questionnaireIdentifiers = questionnaireService.findById(id);
                return ResponseEntity.ok().body(questionnaireIdentifiers);

            }else{

                long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
                QuestionnaireBody questionnaireBody = questionnaireService.findByIdWhereUser(id,userId,filter);
                return ResponseEntity.ok().body(questionnaireBody);

            }
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
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

    @PutMapping("/{qid}")
    public void setEnabled(@PathVariable long qid){

    }

    @DeleteMapping("/{qid}")
    public void deleteQuest(@PathVariable long qid){
      //questionnaireService.deleteById(qid);
   }



}
