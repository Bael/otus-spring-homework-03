package ru.otus.spring.hw3.service;

import ru.otus.spring.hw3.domain.Exam;
import ru.otus.spring.hw3.domain.ExamResult;

public interface ExamService {


    Exam prepareExam(String userName);

    ExamResult checkExam(Exam exam);

}
