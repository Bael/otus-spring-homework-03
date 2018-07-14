package ru.otus.spring.hw3.ui;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.*;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw3.domain.AnswerOption;
import ru.otus.spring.hw3.domain.Exam;
import ru.otus.spring.hw3.domain.ExamResult;
import ru.otus.spring.hw3.domain.Question;
import ru.otus.spring.hw3.service.ExamService;
import ru.otus.spring.hw3.utils.TypedMessageHelper;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.shell.table.CellMatchers.at;

@Service
@ShellComponent
public class ExamUIShellImpl implements ExamUI {

    private final TypedMessageHelper typedMessageHelper;
    private final ExamService examService;
    private Question currentQuestion;
    private Exam exam;


    public ExamUIShellImpl(TypedMessageHelper typedMessageHelper, ExamService examService) {
        this.typedMessageHelper = typedMessageHelper;
        this.examService = examService;
    }

    @ShellMethod(value = "Start quiz for user", key = {"start", "начать"})
    private String start(@Size(min = 1) String username) {
        startExamForUser(username);
        return typedMessageHelper.getLocalizedMessage(TypedMessageHelper.TEXT_WELCOME, null);
    }

    @Override
    public void startExamForUser(String username) {
        exam = examService.prepareExam(username);
    }

    @ShellMethod(value = "Show next question", key = {"следующий", "next"})
    private String next() {
        currentQuestion = exam.getNext();
        return getQuestionText(currentQuestion);
    }

    @ShellMethod(value = "Tell your answer", key = {"ответ", "answer"})
    private void answer(@Size(min = 1) String answerText) {
        exam.putUserAnswers(currentQuestion, parseAnswer(currentQuestion, answerText));
    }

    private List<AnswerOption> parseAnswer(Question q, String input) {

        Set<Long> answers = Stream.of(input.split(",")).map(Long::parseLong).collect(Collectors.toSet());

        return q.getOptions().stream()
                .filter(answerOption -> answers.contains(answerOption.getId()))
                .collect(Collectors.toList());
    }

    @ShellMethod(value = "Show previous question", key = {"предыдущий", "prev"})
    private String prev() {
        currentQuestion = exam.getPrevious();
        return getQuestionText(currentQuestion);
    }

    @ShellMethod(value = "Finish test", key = {"закончить", "finish"})
    private String finish() {
        ExamResult examResult = examService.checkExam(exam);
        return reportResult(examResult);
    }


    private String reportResult(ExamResult result) {

        String message = TypedMessageHelper.TEXT_REPORT_SUCESS_RESULT;

        if (!result.IsPassed()) {
            message = TypedMessageHelper.TEXT_REPORT_FAILURE_RESULT;
        }
        return typedMessageHelper.getLocalizedMessage(message, new String[]{String.valueOf(result.getRate()), String.valueOf(result.getSuccessRate())});
    }

    private String getQuestionText(Question q) {
        final String[][] data;
        final TableModel model;
        final TableBuilder tableBuilder;

        data = new String[q.getOptions().size() + 1][2];
        model = new ArrayTableModel(data);

        tableBuilder = new TableBuilder(model);
        int count = q.getAvailableOptionsCount();
        data[0][0] = typedMessageHelper.getLocalizedMessage(TypedMessageHelper.TEXT_QUESTION, new String[]{""});
        data[0][1] = q.getText() +
                typedMessageHelper.getLocalizedMessage(
                        TypedMessageHelper.TEXT_ASK_SELECT_OPTIONS,
                        new String[]{String.valueOf(count)});

        tableBuilder.on(at(0, 0)).addAligner(SimpleHorizontalAligner.values()[0]);
        tableBuilder.on(at(0, 1)).addAligner(SimpleVerticalAligner.values()[1]);

        for (int i = 0; i < q.getOptions().size(); i++) {
            data[i + 1][0] = i + ")";
            data[i + 1][1] = q.getOptions().get(i).getOptionText();
            tableBuilder.on(at(i, 0)).addAligner(SimpleHorizontalAligner.values()[0]);
            tableBuilder.on(at(i, 1)).addAligner(SimpleVerticalAligner.values()[1]);

        }

        return tableBuilder.addFullBorder(BorderStyle.fancy_light).build().render(80);
    }

    private Availability finishAvailability() {
        return exam != null
                ? Availability.available()
                : Availability.unavailable("you should start the exam before finish!");
    }

    private Availability nextAvailability() {
        if (exam == null) {
            return Availability.unavailable("you need start exam first!");
        }

        return exam.hasNext()
                ? Availability.available()
                : Availability.unavailable("no more questions left!");
    }

    private Availability prevAvailability() {
        if (exam == null) {
            return Availability.unavailable("you need start exam first!");
        }

        return exam.hasPrevious()
                ? Availability.available()
                : Availability.unavailable("no previous questions behead!");
    }

    private Availability answerAvailability() {
        if (exam == null) {
            return Availability.unavailable("you need start exam first!");
        }

        return currentQuestion != null
                ? Availability.available()
                : Availability.unavailable("you should start exam and choose question to answer!");
    }


}
