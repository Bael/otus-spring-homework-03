package ru.otus.spring.hw3.dao;

import org.springframework.stereotype.Service;
import ru.otus.spring.hw3.AppConfig;
import ru.otus.spring.hw3.domain.AnswerOption;
import ru.otus.spring.hw3.domain.Question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

@Service
public class QuestionDAOCSVImpl implements QuestionDAO {

    private final AppConfig config;

    public QuestionDAOCSVImpl(AppConfig config) {
        this.config = config;
    }


    /*
      Предусловие - формат файла жестко задан.

      1 строка заголовок вида: Тип строки, текст, вес (вопроса в целом или выбранного варианта ответа), максимальное количество вариантов выбора
      Тип строки (Question или пусто (для ответа))
      строки идут по порядку - строка с вопросом, потом опции ответа.
      **/

    // simple csv reader. copied from https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
    private ArrayList<String[]> readCSV(File csvFile) {

        String line;
        String cvsSplitBy = ",";
        ArrayList<String[]> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                // use comma as separator

                String[] fields = line.split(cvsSplitBy);
                if (fields.length > 1) {
                    list.add(fields);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Question> loadQuestions() {
        ArrayList<String[]> list = readCSV(config.getCsvFile());
        return getQuestions(list);
    }

    private List<Question> getQuestions(ArrayList<String[]> list) {
        List<Question> questions = new ArrayList<>();


        // пропускаем первую строку с заголовками
        ListIterator<String[]> it = list.listIterator(1);

        HashMap<Integer, ArrayList<String[]>> rawOptionsMap = new HashMap<>();
        HashMap<Integer, String[]> rawQuestionsMap = new HashMap<>();

        int questionsCounter = 0;
        // первый проход группирует записи для упрощенного разбора
        while (it.hasNext()) {
            String[] fields = it.next();

            if (fields[0].equals("Question")) {
                questionsCounter++;
                rawQuestionsMap.put(questionsCounter, fields);
            } else {
                rawOptionsMap.compute(questionsCounter, (integer, strings) -> {
                    if (strings == null) {

                        strings = new ArrayList<>();
                    }
                    strings.add(fields);
                    return strings;
                });
            }
        }

        // формируем доменную модель
        for (int i = 1; i <= questionsCounter; i++) {
            ArrayList<String[]> rawOptions = rawOptionsMap.get(i);
            List<AnswerOption> options = new ArrayList<>();

            for (int j = 0; j < rawOptions.size(); j++) {
                String[] answerRecord = rawOptions.get(j);
                String answerOptionText = answerRecord[1];
                double answerWeight = Double.parseDouble(answerRecord[2]);
                options.add(new AnswerOption(answerOptionText, answerWeight, (long) j));
            }

            String[] questionRecord = rawQuestionsMap.get(i);
            String questionText = questionRecord[1];
            double questionWeight = Double.parseDouble(questionRecord[2].trim());
            int maxOptionsCount = Integer.parseInt(questionRecord[3].trim());
            Question q = new Question(questionWeight, questionText, options, maxOptionsCount);
            questions.add(q);
        }

        return questions;
    }


}
