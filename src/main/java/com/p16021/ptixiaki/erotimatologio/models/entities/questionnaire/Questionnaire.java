package com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.p16021.ptixiaki.erotimatologio.models.entities.identifier.Identifier;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
public class Questionnaire {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name cant be empty")
    private String name;
    private String shortDescription;
    private Boolean enabled;

    @ElementCollection()
    @Enumerated
    private Set<IdentifierType> identifiers;

    @OneToMany(mappedBy = "questionnaire",cascade = CascadeType.ALL)
    @JsonManagedReference(value = "questionGroups")
    private List<QuestionGroup> questionnaire;

    @Transient
    private int numOfResponses;
    @Transient
    private Map<IdentifierType,List<Identifier>> eligibleResponsesIdentifiers;

    public Questionnaire(String name,String shortDescription, Set<IdentifierType> identifiers, List<QuestionGroup> questionnaire) {
        this.name = name;
        this.shortDescription =shortDescription;
        this.identifiers = identifiers;
        this.questionnaire = questionnaire;
    }

    public Questionnaire(String name, List<QuestionGroup> questionnaire) {
        this.name = name;
        this.questionnaire = questionnaire;
    }


}

