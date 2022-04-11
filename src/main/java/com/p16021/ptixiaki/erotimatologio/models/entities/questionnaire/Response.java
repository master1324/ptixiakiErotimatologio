package com.p16021.ptixiaki.erotimatologio.models.entities.questionnaire;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
public class Response {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private Long userId;
    @NotEmpty(message = "Response cant be empty")
    private String response;
    private String filter;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference(value = "responses")
    private Question question;


    public Response(String response, String filter, Question question) {
        this.response = response;
        this.filter = filter;
        this.question = question;
    }


}
