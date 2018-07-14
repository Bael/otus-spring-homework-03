package ru.otus.spring.hw3.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.hw3.QuizConfig;
import ru.otus.spring.hw3.dao.QuestionDAO;
import ru.otus.spring.hw3.domain.Exam;
import ru.otus.spring.hw3.domain.ExamResult;
import ru.otus.spring.hw3.domain.Question;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private final QuizConfig config;
    private QuestionDAO questionDAO;
    private double successRate;


    public ExamServiceImpl(QuestionDAO questionDAO, QuizConfig config) {
        this.questionDAO = questionDAO;
        this.config = config;
        this.successRate = config.getSuccessRate();
    }

    @Override
    public Exam prepareExam(String userName) {
        List<Question> questions = this.questionDAO.loadQuestions();
        return new Exam(userName, questions);
    }

    @Override
    public ExamResult checkExam(Exam exam) {
        return exam.getExamResult(successRate);
    }

}
