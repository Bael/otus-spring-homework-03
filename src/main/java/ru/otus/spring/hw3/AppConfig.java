package ru.otus.spring.hw3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Component
@ConfigurationProperties("application")
public class AppConfig {

    private String filename;

    private Locale locale;

    @Autowired
    private MessageSource messageSource;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Locale getLocaleString() {
        return locale;
    }

    public void setLocale(String localeString) {
        this.locale = detectLocale(localeString);
    }

    private Locale detectLocale(String localeString) {
        switch (localeString.toLowerCase()) {
            case "en":
                return Locale.ENGLISH;
            case "ru":
                return new Locale("RU");
            default:
                return new Locale("RU");
        }
    }

    public String getLocalizedMessage(String messageType, String[] params) {
        return messageSource.getMessage(messageType, params, locale);
    }

    public File getCsvFile() {
        Path filepath = Paths.get("questions", locale.getLanguage().toLowerCase(), filename);
        URL url = getClass().getClassLoader().getResource(filepath.toString());
        return new File(url.getFile());
    }


}
