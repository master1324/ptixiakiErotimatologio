package com.p16021.ptixiaki.erotimatologio.controllers;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Filter;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireBody;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.FilterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
@Slf4j
public class FilterController {

    private final FilterService filterService;

    @GetMapping("/decode")
    public ResponseEntity<Map<IdentifierType,String>> decodeFilter(@RequestParam  String filter)  {
            Map<IdentifierType,String> decodedFilter = filterService.decodeFilter(filter);
            return ResponseEntity.ok().body(decodedFilter);
    }

    @GetMapping("")
    public ResponseEntity<Filter> getFilter(@RequestParam("id") int id, @RequestParam String filter)  {

        return ResponseEntity.ok().body(filterService.getFilter(id,filter));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Filter>> getAllFilters()  {

        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        return ResponseEntity.ok().body(filterService.getAllFilters(userId));
    }


    @PostMapping(value = { "/add" }, consumes= {"application/json"})
    public ResponseEntity<Filter> createFilter(@RequestBody Filter filter){
        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        filterService.saveFilter(filter,userId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("filter/add").toUriString());
        return ResponseEntity.created(uri).build();

    }

    @PutMapping("/update")
    public ResponseEntity<Filter> updateFilter(@RequestBody Filter filter){
        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        filterService.updateFilter(filter,userId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("filter/update").toUriString());
        return ResponseEntity.created(uri).build();

    }

    @GetMapping("/enable")
    public ResponseEntity<Filter> setEnabled(@RequestParam String filter,@RequestParam boolean enabled){

        filterService.setEnabled(filter,enabled);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("filter/enable").toUriString());
        return ResponseEntity.created(uri).build();

    }



}
