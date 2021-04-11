package ru.dipech.listeneng.service;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.entity.Locale;
import ru.dipech.listeneng.exception.ApplicationException;

@Service
public class TranslationServiceImpl implements TranslationService {

    private final LanguageTranslator languageTranslator;

    public TranslationServiceImpl(
        @Value("${app.ibm.api.key}") String ibmApiKey,
        @Value("${app.ibm.api.url}") String ibmApiUrl
    ) {
        var authenticator = new IamAuthenticator(ibmApiKey);
        languageTranslator = new LanguageTranslator("2020-10-01", authenticator);
        languageTranslator.setServiceUrl(ibmApiUrl);
    }

    @Override
    public String translate(String text, Locale inputLocale, Locale outputLocale) {
        var modelId = convertLocaleToCode(inputLocale) + "-" + convertLocaleToCode(outputLocale);
        var translateOptions = new TranslateOptions.Builder()
            .addText(text)
            .modelId(modelId)
            .build();
        var translationResult = languageTranslator.translate(translateOptions)
            .execute().getResult();
        var translations = translationResult.getTranslations();
        if (translations.size() == 0) {
            throw new ApplicationException("Couldn't translate, sorry :(");
        }
        return translations.get(0).getTranslation();
    }

    private String convertLocaleToCode(Locale locale) {
        if (locale.equals(Locale.EN)) {
            return "en";
        }
        if (locale.equals(Locale.RU)) {
            return "ru";
        }
        throw new ApplicationException("Unknown locale");
    }

}
