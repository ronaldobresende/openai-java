package br.com.resende.client;

import br.com.resende.util.ApiKeyUtil;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.Setter;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Setter
public class OpenAIClient {

    private Integer maxTokens;
    private Double temperature;
    private long timeout;

    public OpenAIClient(){
        this.maxTokens = 256;
        this.temperature = 1.0;
        this.timeout = 30L;
    }
    @SneakyThrows
    public List<ChatCompletionChoice> sendRequest(String system, String user, String modelo) {

        var token = ApiKeyUtil.getToken();

        var segundoParaProximaTentiva = 5;
        var tentativas = 0;

        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(this.timeout));

        var chatCompletionRequest = ChatCompletionRequest.builder()
                .model(modelo)
                .maxTokens(maxTokens)
                .temperature(this.temperature)
                .messages(Arrays.asList(
                        new ChatMessage(ChatMessageRole.USER.value(), user),
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), system)
                ))
                .build();

        while (tentativas++ != 5) {
            try {
                return service.createChatCompletion(chatCompletionRequest).getChoices();
            } catch (OpenAiHttpException ex) {
                var errorCode = ex.statusCode;
                switch (errorCode) {
                    case 401 -> throw new RuntimeException("Erro com a chave da API!", ex);
                    case 429 -> {
                        System.out.println("Rate Limit atingido! Nova tentativa em instantes");
                        Thread.sleep(1000 * segundoParaProximaTentiva);
                        segundoParaProximaTentiva *= 2;
                    }
                    case 500, 503 -> {
                        System.out.println("API fora do ar! Nova tentativa em instantes");
                        Thread.sleep(1000 * segundoParaProximaTentiva);
                        segundoParaProximaTentiva *= 2;
                    }
                }
            }
            throw new RuntimeException("API Fora do ar! Tentativas finalizadas sem sucesso!");
        }
        return null;
    }

}
