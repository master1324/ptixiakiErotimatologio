package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Teacher;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import com.p16021.ptixiaki.erotimatologio.models.projections.TeacherProjection;
import com.p16021.ptixiaki.erotimatologio.repos.IdentifierRepo;
import com.p16021.ptixiaki.erotimatologio.repos.TeacherRepo;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.IdentifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType.EXAMINO;
import static com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType.TEACHER;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentifierServiceImpl implements IdentifierService {

    private final IdentifierRepo identifierRepo;
    private final TeacherRepo teacherRepo;

    @Override
    public Identifier findById(long id){
        Optional<Identifier> optional = identifierRepo.findById(id);
        assert optional.isPresent();
        return optional.get();
    }


    @Override
    public List<Identifier> findAll() {
        List<Identifier> identifiers = (List<Identifier>) identifierRepo.findAll();

        for(int j=0;j<identifiers.size();j++){
            Identifier i = identifiers.get(j);
            if(i.getType().equals(TEACHER))
                identifiers.set(j,new Identifier(i.getId(),i.getName(),i.getType()));
        }

        return identifiers;
    }


    @Override
    public Map<IdentifierType,List<Identifier>> findAllGrouped(){

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
    public Map<IdentifierType, List<Identifier>> findAllByUser(long userId) {

        Map<IdentifierType,List<Identifier>> identifierMap = new HashMap<>();



        return null;
    }


    @Override
    public List<String> findEligibleResponses(ResponseType responseType){
        List<String> eligibleResponses = new ArrayList<>();

        switch (responseType){
            case TRUE_FALSE:
                eligibleResponses.add("??????");
                eligibleResponses.add("??????");
                break;
            case MULTI5:
                eligibleResponses.addAll(Arrays.asList("??????????????","????????","????????????","????????","???????? ????????"));
                break;
        }

        return eligibleResponses;
    }

    @Override
    public List<Identifier> findByIdentifierType(IdentifierType identifierType,long userId){
        List<Identifier> identifiers = new ArrayList<>();
        Optional<Teacher> teacherOptional = Optional.empty();
        if(userId!=-1){
            teacherOptional = teacherRepo.findByAppUserId(userId);
        }


        switch (identifierType){
            case SUBJECT:
                if(teacherOptional.isPresent()){
                   identifiers.addAll(teacherOptional.get().getSubjects());
                   break;
                }
                identifiers.addAll(identifierRepo.findAllByType(IdentifierType.SUBJECT));
                break;
            case TEACHER:
                if(teacherOptional.isPresent()){
                    Teacher t = teacherOptional.get();
                    identifiers.add(new Identifier(t.getId(),t.getName(),t.getType()));
                    break;
                }
                List<TeacherProjection> teacherProjections = teacherRepo.findBy();
                identifiers.addAll(setUpTeacher(teacherProjections));
                break;
            case DEPARTMENT:
                if(teacherOptional.isPresent()){
                    identifiers.addAll(teacherOptional.get().getDepartments());
                    break;
                }
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

    private List<Identifier> setUpTeacher(List<TeacherProjection> projections){
        List<Identifier> identifierList = new ArrayList<>();
        projections.forEach(pro-> identifierList.add(new Identifier(pro.getId(),pro.getName(),pro.getType())));
        return identifierList;
    }

}
