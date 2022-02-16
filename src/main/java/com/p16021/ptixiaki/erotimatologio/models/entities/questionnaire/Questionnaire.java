package com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.p16021.ptixiaki.erotimatologio.models.enums.IdentifierType;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
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

    @ElementCollection()
    @Enumerated
    private Set<IdentifierType> identifiers;

    @OneToMany(mappedBy = "questionnaire",cascade = CascadeType.ALL)
    @JsonManagedReference(value = "questionGroups")
    private List<QuestionGroup> questionnaire;

    //TODO : kane costum validator gia tin singrisi
    @Min(1)@Max(12)
    private int fromMonth;
    @Min(1)@Max(12)
    private int toMonth;

    @Transient
    private Map<IdentifierType,List<Identifier>> eligibleResponsesIdentifiers;

    public Questionnaire(String name, Set<IdentifierType> identifiers, List<QuestionGroup> questionnaire) {
        this.name = name;
        this.identifiers = identifiers;
        this.questionnaire = questionnaire;
    }

    public Questionnaire(String name, List<QuestionGroup> questionnaire) {
        this.name = name;
        this.questionnaire = questionnaire;
    }


}

