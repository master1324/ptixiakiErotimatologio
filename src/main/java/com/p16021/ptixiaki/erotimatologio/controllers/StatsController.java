package com.p16021.ptixiaki.erotimatologio.controllers;

import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireBody;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireIdentifiers;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quest/{qid}")
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @GetMapping("stats")
    public ResponseEntity<QuestionnaireView> getById(@PathVariable("qid") int id, @RequestParam String filter)  {
        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        return null;
    }
}
