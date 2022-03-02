package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireValidators;
import com.p16021.ptixiaki.erotimatologio.repos.IdentifierRepo;
import com.p16021.ptixiaki.erotimatologio.repos.QuestionnaireRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.FilterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;

import static com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType.YEAR;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilterServiceImpl implements FilterService {

    private final IdentifierRepo identifierRepo;
    private final QuestionnaireRepo questionnaireRepo;

    @Override
    public boolean filterIsOk(String filter, Long qid){
        //QuestionnaireValidators questionnaireValidators = questionnaireRepo.findProjectedById(qid,QuestionnaireValidators.class);
        int year = Year.now().getValue();

        String[] filterArray= filter.split(",");

        if (filterArray[0].equals(String.valueOf(year))){
            return true;
        }else{
            throw new RuntimeException("Mi apodekto filter");
        }


    }

    @Override
    public Map<IdentifierType,String> decodeFilter(String filter){
        Map<IdentifierType,String> decodedFilter = new HashMap<>();
        List<String> items = Arrays.asList(filter.split("\\s*,\\s*"));
        decodedFilter.put(YEAR,items.get(0));
        items.forEach( item ->{
            Optional<Identifier> identifier = identifierRepo.findById(Long.parseLong(item));
            identifier.ifPresent(value -> decodedFilter.put(value.getType(), value.getName()));
        });

        return decodedFilter;
    }

    @Override
    public String produceFilter(){

        return "";
    }
}
