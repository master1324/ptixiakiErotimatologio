package com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.p16021.ptixiaki.erotimatologio.models.enums.ResponseType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
public class Question {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Question cant be empty")
    private String question;

    @OneToMany(mappedBy = "question")
    @JsonManagedReference(value = "responses")
    private List<Response> responses;

    @ManyToOne
    @JoinColumn(name = "question_group_id")
    @JsonBackReference(value = "questions")
    private QuestionGroup questionGroup;

    //set by service
    @Transient
    private String result;
    @Transient
    private Long responseId;
    @Transient
    private String userResponse;
    @Transient
    private List<String> eligibleResponses;

    public Question(Long id) {
        this.id = id;
    }
}
