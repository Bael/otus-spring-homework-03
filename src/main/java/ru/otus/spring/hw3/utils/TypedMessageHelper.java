package ru.otus.spring.hw3.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw3.AppConfig;

@Service
public class TypedMessageHelper {

    public static final String TEXT_WELCOME = "test.welcome";
    public static final String TEXT_ASK_USER_NAME = "test.askUserName";
    public static final String TEXT_SHOW_RULES_OF_ANSWER = "test.showRulesOfAnswering";
    public static final String TEXT_QUESTION = "test.questionName";
    public static final String TEXT_ASK_SELECT_OPTIONS = "test.askSelectOptions";
    public static final String TEXT_REPORT_SUCESS_RESULT = "test.reportSucessResult";
    public static final String TEXT_REPORT_FAILURE_RESULT = "test.reportFailureResult";
    private final AppConfig config;

    @Autowired
    private TypedMessageHelper(AppConfig config) {
        this.config = config;
    }



    public String getLocalizedMessage(String messageType, String[] params) {
        return config.getLocalizedMessage(messageType, params);
    }

}
