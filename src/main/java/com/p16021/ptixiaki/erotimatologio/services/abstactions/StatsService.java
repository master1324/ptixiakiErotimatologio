package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Stats;

public interface StatsService {

    Stats getStats(long questionnaireId,String filter);

}
