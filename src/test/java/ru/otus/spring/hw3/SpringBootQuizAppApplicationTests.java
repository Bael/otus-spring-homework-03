package ru.otus.spring.hw3;

import org.junit.Before;
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
import ru.otus.spring.hw3.domain.Question;
import ru.otus.spring.hw3.service.ExamService;
import ru.otus.spring.hw3.service.ExamServiceImpl;
import ru.otus.spring.hw3.ui.ExamUI;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootQuizAppApplicationTests {

	@Test
	public void contextLoads() {
	}


    @Autowired
    ExamService examService;
    @MockBean
    QuestionDAO questionDAOCSV;
    @MockBean
    QuizConfig quizConfig;
    @MockBean
    ExamUI examUI;

    @Test
    public void examService() {

    }

    @Before
    public void setUp() {
        List<AnswerOption> options = new ArrayList<>();
        AnswerOption rightAnswer = new AnswerOption("Мыши", 1, 3);

        options.add(new AnswerOption("Люди", 0, 1));
        options.add(new AnswerOption("Дельфины", 0, 2));
        options.add(rightAnswer);
        options.add(new AnswerOption("Верблюды", 0, 4));

        Question question = new Question(10, "Назовите самых разумных созданий на земле согласно кнгие 'Автостопом по галактике' Дугласа Адамса.", options, 1);
        List<Question> questions = new ArrayList<>();
        questions.add(question);

        Mockito.when(questionDAOCSV.loadQuestions())
                .thenReturn(questions);

        Mockito.when(quizConfig.getSuccessRate()).thenReturn(40.0);


        List<AnswerOption> rightOptions = new ArrayList<>();
        rightOptions.add(rightAnswer);

        Mockito.when(examUI.askQuestion(question)).thenReturn(rightOptions);


    }

    @TestConfiguration
    class QuizAppTestContextConfiguration {

        @Bean
        public ExamService examService() {
            return new ExamServiceImpl(questionDAOCSV, examUI, quizConfig);
        }

    }


}
