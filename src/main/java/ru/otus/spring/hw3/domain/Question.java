package ru.otus.spring.hw3.domain;

import java.util.Collections;
import java.util.List;

public class Question {

    //private final long id;
    private final double weight;
    private final String text;
    private final List<AnswerOption> options;
    private final double maxScore;
    private final int availableOptionsCount;

    public Question(double weight, String text, List<AnswerOption> options, int availableOptionsCount) {
        this.weight = weight;
        this.text = text;
        this.options = Collections.unmodifiableList(options);
        this.maxScore = getScore(options);
        this.availableOptionsCount = availableOptionsCount;
    }

    double getWeight() {
        return weight;
    }

    public String getText() {
        return text;
    }

    public int getAvailableOptionsCount() {
        return availableOptionsCount;
    }

    private double getScore(List<AnswerOption> options) {
        double result = 0;
        for (AnswerOption option : options) {
            result += option.getWeight();
        }
        return result;
    }

    // возвращаем долю правильных ответов, нормализованную к весу вопроса
    double getUserScore(List<AnswerOption> userAnswers) {
        if (maxScore == 0)
            return 0;

        return weight * (getScore(userAnswers) / maxScore);
    }

    public List<AnswerOption> getOptions() {
        return options;
    }
}
