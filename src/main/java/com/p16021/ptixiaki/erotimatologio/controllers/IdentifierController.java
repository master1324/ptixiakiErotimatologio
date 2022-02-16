package com.p16021.ptixiaki.erotimatologio.controllers;


import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.services.IdentifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/identifiers")
@RequiredArgsConstructor
@Slf4j
public class IdentifierController {

    private final IdentifierService identifierService;

    @GetMapping("/all")
    public ResponseEntity<Map<IdentifierType, List<Identifier>>> getIdentifiers(){

        return ResponseEntity.ok().body(identifierService.findAll());
    }
}
