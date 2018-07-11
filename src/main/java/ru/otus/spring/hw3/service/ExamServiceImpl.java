package ru.otus.spring.hw3.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.hw3.QuizConfig;
import ru.otus.spring.hw3.dao.QuestionDAO;
import ru.otus.spring.hw3.domain.AnswerOption;
import ru.otus.spring.hw3.domain.Exam;
import ru.otus.spring.hw3.domain.Question;
import ru.otus.spring.hw3.ui.ExamUI;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private final QuizConfig config;
    private QuestionDAO questionDAO;
    private ExamUI examUI;
    private double successRate;


    public ExamServiceImpl(QuestionDAO questionDAO, ExamUI examUI, QuizConfig config) {
        this.questionDAO = questionDAO;
        this.examUI = examUI;
        this.config = config;
        this.successRate = config.getSuccessRate();

    }

    @Override
    public void examine() {

        String userName = examUI.getUserName();

        List<Question> questions = this.questionDAO.loadQuestions();
        Exam exam = new Exam(userName, questions);


        while (exam.hasNext()) {

            Question q = exam.getNext();
            List<AnswerOption> answerOptions = examUI.askQuestion(q);
            exam.putUserAnswers(q, answerOptions);
        }

        examUI.reportResult(exam.getExamResult(successRate));

    }

}
