package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireValidators;
import com.p16021.ptixiaki.erotimatologio.repos.IdentifierRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilterService {

    private final IdentifierRepo identifierRepo;

    public boolean filterIsOk(String filter, QuestionnaireValidators questionnaireValidators){

        return true;
    }

    public Map<IdentifierType,String> decodeFilter(String filter){
        Map<IdentifierType,String> decodedFilter = new HashMap<>();
        List<String> items = Arrays.asList(filter.split("\\s*,\\s*"));

        items.forEach( item ->{

            Optional<Identifier> identifier = identifierRepo.findById(Long.parseLong(item));
            identifier.ifPresent(value -> decodedFilter.put(value.getType(), value.getName()));
        });

        return decodedFilter;
    }

}
