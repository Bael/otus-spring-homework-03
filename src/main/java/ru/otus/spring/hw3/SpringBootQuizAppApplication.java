package ru.otus.spring.hw3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.spring.hw3.dao.QuestionDAO;
import ru.otus.spring.hw3.dao.QuestionDAOCSVImpl;
import ru.otus.spring.hw3.service.ExamService;

@SpringBootApplication
@PropertySource("classpath:application.yml")
@Configuration
@ComponentScan("ru.otus.spring.hw3")
public class SpringBootQuizAppApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(SpringBootQuizAppApplication.class, args);

		try {
			ExamService examService = ctx.getBean(ExamService.class);
			examService.examine();
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}




	@Autowired
	QuizConfig quizConfig;
	@Bean
	public QuestionDAO questionDAO() {
		return new QuestionDAOCSVImpl(quizConfig);
	}

	// i18n
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/bundle");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
