package ru.otus.spring.hw3;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ru.otus.spring.hw3.TestApplicationRunner.class)
public class SpringBootQuizAppApplicationTests {


    @MockBean
    QuestionDAO questionDAOCSV;
    @MockBean
    QuizConfig quizConfig;
    private ExamService examService;
    private Exam exam;

    @Test
    public void examServiceAllowsPass() {

        System.out.println(examService);
        System.out.println(exam);

        Question q = exam.getNext();
        assertEquals(false, exam.hasNext());
        List<AnswerOption> rightOptions = q.getOptions()
                .stream()
                .filter(answerOption -> answerOption.getOptionText().equals("Мыши"))
                .collect(Collectors.toList());

        exam.putUserAnswers(q, rightOptions);

        ExamResult result = examService.checkExam(exam);
        assertEquals("Test must me passed!", true, result.IsPassed());
    }


    @Before
    public void setUp() throws Exception {

        examService = new ExamServiceImpl(questionDAOCSV, quizConfig);
        List<AnswerOption> options = new ArrayList<>();

        options.add(new AnswerOption("Люди", 0, 1));
        options.add(new AnswerOption("Дельфины", 0, 2));
        options.add(new AnswerOption("Мыши", 1, 3));
        options.add(new AnswerOption("Верблюды", 0, 4));

        Question question = new Question(1, "Назовите самых разумных созданий на земле согласно кнгие 'Автостопом по галактике' Дугласа Адамса.", options, 1);
        List<Question> questions = new ArrayList<>();
        questions.add(question);

        Mockito.when(questionDAOCSV.loadQuestions())
                .thenReturn(questions);

        Mockito.when(quizConfig.getSuccessRate()).thenReturn(100.0);


        exam = examService.prepareExam("Michael");
        assertEquals(false, exam.hasPrevious());
        assertEquals(true, exam.hasNext());

    }

    @Test
    public void examServiceShouldFail() {

        Question q = exam.getNext();
        assertEquals(false, exam.hasNext());

        List<AnswerOption> rightOptions = q.getOptions()
                .stream()
                .filter(answerOption -> answerOption.getOptionText().equals("Люди"))
                .collect(Collectors.toList());
        exam.putUserAnswers(q, rightOptions);

        ExamResult result = examService.checkExam(exam);
        assertEquals("Test must me failed!", false, result.IsPassed());
    }


}
