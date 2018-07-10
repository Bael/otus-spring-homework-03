package ru.otus.spring.hw3.dao;

import ru.otus.spring.hw3.domain.Question;


import java.util.List;

public interface QuestionDAO {

    List<Question> loadQuestions();
}
