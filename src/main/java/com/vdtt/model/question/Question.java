package com.vdtt.model.question;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Question {
    private String question;
    private List<Answer> answers;
    @Setter
    private int correctAnswer;

    public Question(String question) {
        this.question = question;
        this.answers = new ArrayList<>();
    }

    public Question(String question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }
}
