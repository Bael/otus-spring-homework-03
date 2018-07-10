package ru.otus.spring.hw3.ui;

import org.springframework.stereotype.Service;
import ru.otus.spring.hw3.domain.AnswerOption;
import ru.otus.spring.hw3.domain.ExamResult;
import ru.otus.spring.hw3.domain.Question;
import ru.otus.spring.hw3.utils.LocaleHelper;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExamUIConsoleImpl implements ExamUI {


    private Scanner in;
    private String userName;
    private LocaleHelper localeHelper;

    public ExamUIConsoleImpl(LocaleHelper localeHelper) {
        this.localeHelper = localeHelper;
    }

    /// извлекаем выбранные опции
    private List<AnswerOption> parseAnswer(Question q, String input) {

        Set<Long> answers = Stream.of(input.split(",")).map(Long::parseLong).collect(Collectors.toSet());

        return q.getOptions().stream()
                .filter(answerOption -> answers.contains(answerOption.getId()))
                .collect(Collectors.toList());
    }


    @Override
    public List<AnswerOption> askQuestion(Question q) {
        String input = getAnswer(q);
        return parseAnswer(q, input);
    }

    @Override
    public String getUserName() {

        in = new Scanner(System.in);

        printlnText(LocaleHelper.TEXT_WELCOME, null);

        do {
            printlnText(LocaleHelper.TEXT_ASK_USER_NAME, null);
            userName = in.nextLine();
        } while (userName.trim().equals(""));
        return userName;
    }


    /// верной считаем строку с перечислением номеров через запятую
    private boolean isValid(String input, Question question) {
        if (input.equals(""))
            return false;

        // to do add checking max available count
        return input.replace(" ", "").matches("(\\d,)*\\d,?");
    }

    private String getAnswer(Question question) {
        String input;
        do {
            printQuestion(question);
            input = in.nextLine();
            if (isValid(input, question)) {
                break;
            } else {
                printText(LocaleHelper.TEXT_SHOW_RULES_OF_ANSWER, null);

            }
        } while (true);

        return input;
    }

    private void printQuestion(Question question) {
        printlnText(LocaleHelper.TEXT_ASK_SELECT_OPTIONS, new String[]{String.valueOf(question.getAvailableOptionsCount())});

        question.getOptions().forEach(answerOption -> System.out.println(answerOption.toString()));
    }

    @Override
    public void reportResult(ExamResult result) {

        String message = LocaleHelper.TEXT_REPORT_SUCESS_RESULT;

        if (!result.IsPassed()) {
            message = LocaleHelper.TEXT_REPORT_FAILURE_RESULT;
        }

        printlnText(message, new String[]{String.valueOf(result.getRate()), String.valueOf(result.getSuccessRate())});

    }

    private void printText(String messageType, String[] params) {
        System.out.print(localeHelper.getLocalizedMessage(messageType, params));
    }

    private void printlnText(String messageType, String[] params) {
        System.out.println(localeHelper.getLocalizedMessage(messageType, params));
    }

}
