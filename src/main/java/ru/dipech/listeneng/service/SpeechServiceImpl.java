package ru.dipech.listeneng.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.entity.Locale;
import ru.dipech.listeneng.exception.ApplicationException;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class SpeechServiceImpl implements SpeechService {

    private final String awsApiKey;
    private final String awsApiSecret;
    private final AmazonPollyClient client;
    private final List<Voice> voices;

    public SpeechServiceImpl(
        @Value("${app.aws.api.key}") String awsApiKey,
        @Value("${app.aws.api.secret}") String awsApiSecret
    ) {
        this.awsApiKey = awsApiKey;
        this.awsApiSecret = awsApiSecret;
        this.client = getClient();
        this.voices = getVoices();
    }

    @Override
    public InputStream speech(String text, Locale locale) {
        Voice voice = getVoiceByLocale(locale);
        SynthesizeSpeechRequest speechRequest = new SynthesizeSpeechRequest()
            .withText(text)
            .withVoiceId(voice.getId())
            .withOutputFormat(OutputFormat.Mp3);
        return new BufferedInputStream(client.synthesizeSpeech(speechRequest).getAudioStream());
    }

    private Voice getVoiceByLocale(Locale locale) {
        if (locale.equals(Locale.RU)) {
            // Names: "Maxim", "Tatyana"
            return getVoice(Locale.RU, "Tatyana");
        } else if (locale.equals(Locale.EN)) {
            // Names: "Salli", "Joanna", "Matthew", "Ivy", "Justin", "Kendra", "Kimberly", "Joey"
            return getVoice(Locale.EN, "Joanna");
        }
        throw new ApplicationException("Only RU and EN locales currently supported");
    }

    private AmazonPollyClient getClient() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(awsApiKey, awsApiSecret);

//        var client = new AmazonPollyClient(new AWSStaticCredentialsProvider(credentials),
//            new ClientConfiguration());
//        client.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));

        return (AmazonPollyClient) AmazonPollyClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.EU_CENTRAL_1).build();
    }

    private Voice getVoice(Locale locale, String voiceName) {
        Optional<Voice> voiceOptional = voices.stream()
            .filter(voice -> voice.getLanguageCode().equals(locale.getCode()))
            .filter(voice -> voice.getName().equals(voiceName))
            .findFirst();
        if (voiceOptional.isEmpty()) {
            throw new ApplicationException("Can't find voice");
        }
        return voiceOptional.get();
    }

    private List<Voice> getVoices() {
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();
        DescribeVoicesResult describeVoicesResult = client.describeVoices(describeVoicesRequest);
        return describeVoicesResult.getVoices();
    }

}
