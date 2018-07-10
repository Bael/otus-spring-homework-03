package ru.otus.spring.hw3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class QuizConfig {
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public QuizConfigSettings getQuizConfig() {
        return quizConfig;
    }

    public void setQuizConfig(QuizConfigSettings quizConfig) {
        this.quizConfig = quizConfig;
    }

    private String filename;
    private String locale;
    public static class QuizConfigSettings {
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

        int questionsMaxCount;
        double successRate;
    }

    private QuizConfigSettings quizConfig;


}
