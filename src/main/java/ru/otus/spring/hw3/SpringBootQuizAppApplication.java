package ru.otus.spring.hw3;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.shell.jline.PromptProvider;


@SpringBootApplication
@PropertySource("classpath:application.yml")
@Configuration
public class SpringBootQuizAppApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(SpringBootQuizAppApplication.class, args);

		/*
		try {
			ExamService examService = ctx.getBean(ExamService.class);
			examService.examine();
			ctx.close();
		} catch (Exception ex) {
			ex.printStackTrace();

		}*/
	}

	// i18n
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/bundle");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public PromptProvider myPromptProvider() {
		return () -> new AttributedString("quiz-shell:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
	}
}
