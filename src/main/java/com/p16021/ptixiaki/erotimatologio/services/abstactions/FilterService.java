package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Filter;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;

import java.util.Map;

public interface FilterService {
    boolean filterIsOk(String filter, Long qid);

    Map<String, String> decodeFilter(String filter);

    String produceFilter(String[] ids);

    void saveFilter(Filter filter, long userId);

    void updateFilter(Filter filter,long userId);

    void setEnabled(String filter,boolean enabled);

    Iterable<Filter> getAllFilters(long userId);

    Filter getFilter(long questionnaireId,String filter);
}
