package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Stats;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.StatsService;
import org.springframework.stereotype.Service;

@Service
public class StatsServiceI implements StatsService {

    @Override
    public Stats getStats(long questionnaireId, String filter) {
        return null;
    }
}
