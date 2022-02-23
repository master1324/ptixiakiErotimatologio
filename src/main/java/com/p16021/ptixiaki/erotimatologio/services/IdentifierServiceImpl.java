package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import com.p16021.ptixiaki.erotimatologio.repos.IdentifierRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.IdentifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType.EXAMINO;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentifierServiceImpl implements IdentifierService {

    private final IdentifierRepo identifierRepo;

    @Override
    public Identifier findById(long id){
        Optional<Identifier> optional = identifierRepo.findById(id);
        assert optional.isPresent();
        return optional.get();
    }

    @Override
    public Map<IdentifierType,List<Identifier>> findAll(){

        Map<IdentifierType,List<Identifier>> identifierMap = new HashMap<>();
        List<Identifier> identifiers = (List<Identifier>) identifierRepo.findAll();

        Set<IdentifierType> set = EnumSet.allOf(IdentifierType.class);
        //identifierMap.keySet().addAll(set);

        for(IdentifierType type: set){
            List<Identifier> ids = identifiers.stream().filter(id -> id.getType().equals(type)).collect(Collectors.toList());
            identifierMap.put(type,ids);
        }

        return identifierMap;
    }

    @Override
    public List<String> findEligibleResponses(ResponseType responseType){
        List<String> eligibleResponses = new ArrayList<>();

        switch (responseType){
            case TRUE_FALSE:
                eligibleResponses.add("NAI");
                eligibleResponses.add("OXI");
                break;
            case MULTI5:
                eligibleResponses.addAll(Arrays.asList("KATHOLOU","LIGO","ARKETA","POLI","PARA POLI"));
                break;
        }

        return eligibleResponses;
    }

    @Override
    public List<Identifier> findByIdentifierType(IdentifierType identifierType){
        List<Identifier> identifiers = new ArrayList<>();
        switch (identifierType){
            case SUBJECT:
                identifiers.addAll(identifierRepo.findAllByType(IdentifierType.SUBJECT));
                break;
            case TEACHER:
                identifiers.addAll(identifierRepo.findAllByType(IdentifierType.TEACHER));
                break;
            case DEPARTMENT:
                identifiers.addAll(identifierRepo.findAllByType(IdentifierType.DEPARTMENT));
                break;
            case EXAMINO:
                identifiers.addAll(identifierRepo.findAllByType(EXAMINO));
                break;
            case SEMESTER:
                identifiers.addAll(identifierRepo.findAllByType(IdentifierType.SEMESTER));
                break;
        }
        return identifiers;
    }

}
