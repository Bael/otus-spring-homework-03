package ru.otus.spring.hw3.domain;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;


public class Exam {

    private final String userName;
    private final List<Question> questions;

    private final double availableScore;
    private final ListIterator<Question> iterator;
    private final HashMap<Question, List<AnswerOption>> userAnswers;
    private double userScore = 0;

    public Exam(String userName, List<Question> questions) {
        this.userName = userName;
        this.questions = questions;
        // calc max available score
        this.availableScore = questions.stream().mapToDouble(Question::getWeight).sum();

        this.iterator = questions.listIterator();
        userAnswers = new HashMap<>();
    }

    public void putUserAnswers(Question question, List<AnswerOption> selectedOptions) {
        userAnswers.put(question, selectedOptions);
    }

    public Question getNext() {
        return this.iterator.next();
    }

    public Boolean hasNext() {
        return this.iterator.hasNext();
    }

    public Boolean hasPrevious() {
        return this.iterator.hasPrevious();
    }

    public Question getPrevious() {
        return this.iterator.previous();
    }

    // userScore / availableScore * 100 - rounded to %NN.NN
    private double getPercentScore() {
        if (availableScore == 0)
            return 0;

        userAnswers.forEach((question, userAnswers) -> userScore += question.getUserScore(userAnswers));

        final double v = Math.round(userScore / availableScore * 10000);
        return v / 100;
    }

    // userScore / availableScore * 100 - rounded to %NN.NN
    public ExamResult getExamResult(double succesRate) {
        return new ExamResult(getPercentScore(), succesRate);
    }



}
