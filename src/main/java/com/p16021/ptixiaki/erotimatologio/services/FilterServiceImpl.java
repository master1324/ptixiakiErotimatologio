package com.p16021.ptixiaki.erotimatologio.services;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Teacher;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Filter;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Question;
import com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire.Questionnaire;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.projections.questionnaire.QuestionnaireView;
import com.p16021.ptixiaki.erotimatologio.repos.*;
import com.p16021.ptixiaki.erotimatologio.services.abstactions.FilterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.*;

import static com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType.TEACHER;
import static com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType.YEAR;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilterServiceImpl implements FilterService {

    private final IdentifierRepo identifierRepo;
    private final FilterRepo filterRepo;
    private final TeacherRepo teacherRepo;
    private final QuestionnaireRepo questionnaireRepo;
    private final QuestionRepo questionRepo;


    @Override
    public boolean filterIsOk(String filter, Long qid){
        //QuestionnaireValidators questionnaireValidators = questionnaireRepo.findProjectedById(qid,QuestionnaireValidators.class);

        boolean filterExists = filterRepo.existsByFilterAndQuestionnaireIdAndEnabled(filter,qid,true);

        if (filterExists){
            int year = Year.now().getValue();

            String[] filterArray= filter.split(",");

            if (filterArray[0].equals(String.valueOf(year))){
                return true;
            }else{
                throw new RuntimeException("Mi apodekto filter");
            }
        }

        return false;
    }

    @Override
    public Map<String,String> decodeFilter(String filter){

        Map<String,String> decodedFilter = new HashMap<>();
        List<String> items = Arrays.asList(filter.split("\\s*,\\s*"));
        decodedFilter.put(YEAR.name(),items.get(0));
        items.forEach( item ->{
            Optional<Identifier> identifier = identifierRepo.findById(Long.parseLong(item));
            identifier.ifPresent(value -> decodedFilter.put(value.getType().name(), value.getName()));
        });

        return decodedFilter;
    }

    @Override
    public String produceFilter(String[] ids){
        Arrays.sort(ids);
        String filter = String.join(",", ids);
        filter = Calendar.getInstance().get(Calendar.YEAR) +","+filter;
        return  filter;
    }

    @Override
    public void saveFilter(Filter filter, long userId) {
        boolean filterExists =
                filterRepo
                        .existsByFilterAndQuestionnaireId(
                                filter.getFilter(),
                                filter.getQuestionnaireId());
        if(!filterExists){
            filter.setUserId(userId);
            filterRepo.save(filter);
        }
        //TODO:filter exists error
    }

    @Override
    @Transactional
    public void updateFilter(Filter filter, long userId) {
        filter.setUserId(userId);
        filterRepo.save(filter);
    }

    @Override
    public void setEnabled(String filter, boolean enabled) {
        Optional<Filter> f = filterRepo.findByFilter(filter);
        if(f.isPresent()){
            f.get().setEnabled(enabled);
            filterRepo.save(f.get());
        }

    }

    @Override
    public Iterable<Filter> getAllFilters(long userId) {

        boolean isTeacher = teacherRepo.existsByAppUserId(userId);
        Iterable<Filter> filters = filterRepo.findAll();
        if(!isTeacher){
            for (Filter f : filters) {
                f.setDecodedFilter(decodeFilter(f.getFilter()));

                //Iterable<Integer> questions = questionnaireRepo.questionsOfQuestionnaire(f.getQuestionnaireId());
                //f.setNumOfResponses(responseRepo.countByFilterAndQuestionIdIn(f.getFilter(), questions));
            }
            return filters;
        }else {

            Optional<Teacher> teacherOptional = teacherRepo.findByAppUserId(userId);
            List<Filter> filters1 = new ArrayList<>();
            if(teacherOptional.isEmpty()){
                throw new RuntimeException("Error");
            }

            for(Filter f:filters){
                Map<String,String> decodedFilter = decodeFilter(f.getFilter());
                boolean hasValue = decodedFilter.containsValue(teacherOptional.get().getName());

                if(hasValue){
                    f.setDecodedFilter(decodedFilter);
                    filters1.add(f);
                }

            }
            return filters1;
        }
    }

    @Override
    public Filter getFilter(long questionnaireId, String filter) {
        Filter f = null;

        int questionId = questionnaireRepo.questionsOfQuestionnaire(questionnaireId);
        Question question = questionRepo.findById(questionId,Question.class);
        long numOfResponses = question.getResponses().stream().filter(response -> response.getFilter().equals(filter)).count();

        Optional<Filter> filterOptional = filterRepo.findByFilterAndQuestionnaireId(filter,questionnaireId);

        if (filterOptional.isPresent()){
            f = filterOptional.get();
            f.setNumOfResponses(numOfResponses);

        }

        return f;
    }
}
