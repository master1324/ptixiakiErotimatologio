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
@Getter
@Setter
public class QuestionnaireResponse {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long questionnaireId;
    private Long userId;
    private String filter;
    private String name;

    @Transient
    private Map<String,String> decodedFilter;

    public QuestionnaireResponse(Long questionnaireId, Long userId, String filter,String name) {
        this.questionnaireId = questionnaireId;
        this.userId = userId;
        this.filter = filter;
        this.name=name;
    }
}
