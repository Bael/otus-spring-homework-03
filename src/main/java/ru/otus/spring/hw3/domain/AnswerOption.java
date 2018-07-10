package ru.otus.spring.hw3.domain;

public class AnswerOption {

    private final long id;
    private final double weight;
    private final String optionText;

    public AnswerOption(String optionText, double weight, long id) {
        this.weight = weight;
        this.optionText = optionText;
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public long getId() {
        return id;
    }

    double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%d) %s", id, optionText);
    }
}
