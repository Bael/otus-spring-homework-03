package ru.otus.spring.hw3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("quiz-config")
public class QuizConfig {


    private int questionsMaxCount;
    private double successRate;

    public int getQuestionsMaxCount() {
        return questionsMaxCount;
    }

    public void setQuestionsMaxCount(int questionsMaxCount) {
        this.questionsMaxCount = questionsMaxCount;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

}
