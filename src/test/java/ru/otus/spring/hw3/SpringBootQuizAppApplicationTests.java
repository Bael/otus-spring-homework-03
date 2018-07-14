package ru.otus.spring.hw3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring.hw3.dao.QuestionDAO;
import ru.otus.spring.hw3.domain.AnswerOption;
import ru.otus.spring.hw3.domain.Exam;
import ru.otus.spring.hw3.domain.ExamResult;
import ru.otus.spring.hw3.domain.Question;
import ru.otus.spring.hw3.service.ExamService;
import ru.otus.spring.hw3.service.ExamServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootQuizAppApplicationTests {


    @Autowired
    ExamService examService;

    @MockBean
    QuestionDAO questionDAOCSV;
    @MockBean
    QuizConfig quizConfig;

    private Exam exam;

    private List<AnswerOption> rightOptions = new ArrayList<>();


    @Test
    public void examService() {

        List<AnswerOption> options = new ArrayList<>();
        AnswerOption rightAnswer = new AnswerOption("Мыши", 1, 3);

        options.add(new AnswerOption("Люди", 0, 1));
        options.add(new AnswerOption("Дельфины", 0, 2));
        options.add(rightAnswer);
        options.add(new AnswerOption("Верблюды", 0, 4));

        Question question = new Question(1, "Назовите самых разумных созданий на земле согласно кнгие 'Автостопом по галактике' Дугласа Адамса.", options, 1);
        List<Question> questions = new ArrayList<>();
        questions.add(question);

        Mockito.when(questionDAOCSV.loadQuestions())
                .thenReturn(questions);

        Mockito.when(quizConfig.getSuccessRate()).thenReturn(100.0);
        rightOptions.add(rightAnswer);

        exam = examService.prepareExam("Michael");
        assertEquals(false, exam.hasPrevious());
        assertEquals(true, exam.hasNext());

        Question q = exam.getNext();
        assertEquals(false, exam.hasNext());

        exam.putUserAnswers(q, rightOptions);

        ExamResult result = examService.checkExam(exam);
        assertEquals(true, result.IsPassed());

    }


    @TestConfiguration
    class QuizAppTestContextConfiguration {

        @Bean
        public ExamService examService() {
            return new ExamServiceImpl(questionDAOCSV, quizConfig);
        }

    }


}
