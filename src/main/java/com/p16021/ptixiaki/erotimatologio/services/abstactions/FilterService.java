package com.p16021.ptixiaki.erotimatologio.services.abstactions;

import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;

import java.util.Map;

public interface FilterService {
    boolean isOk(String filter, Long qid);

    Map<IdentifierType, String> decodeFilter(String filter);

    String produceFilter();
}
