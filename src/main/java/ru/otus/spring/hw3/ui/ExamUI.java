package ru.otus.spring.hw3.ui;

import ru.otus.spring.hw3.domain.AnswerOption;
import ru.otus.spring.hw3.domain.ExamResult;
import ru.otus.spring.hw3.domain.Question;

import java.util.List;

public interface ExamUI {

    String getUserName();

    List<AnswerOption> askQuestion(Question q);

    void reportResult(ExamResult result);

}
