package com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire;

import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Filter {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private long activeFor;
    private long questionnaireId;
    private String filter;
    private long userId;
    private boolean enabled = true;
    private String questionnaireName;
    @Transient
    private Map<IdentifierType,String> decodedFilter;
    @Transient
    private int numOfResponses;

    public Filter(long activeFor, long questionnaireId, long userId, String filter) {
        this.activeFor = activeFor;
        this.questionnaireId = questionnaireId;
        this.userId = userId;
        this.filter = filter;
    }
}
