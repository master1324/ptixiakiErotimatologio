package com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import lombok.*;
import org.eclipse.persistence.annotations.PrivateOwned;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
public class QuestionGroup {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    @JsonBackReference(value = "questionGroups")
    private Questionnaire questionnaire;


    @OneToMany(mappedBy = "questionGroup",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference(value = "questions")
    @PrivateOwned
    private List<Question> questions;


    private ResponseType responseType;

    @Transient
    private List<String> eligibleResponses;

    public QuestionGroup(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }


}
