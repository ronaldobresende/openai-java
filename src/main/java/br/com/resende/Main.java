package br.com.resende;

import br.com.resende.client.OpenAIClient;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.utils.TikTokensUtil;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var system = "Você é um gerente de um grande banco no Brasil, de nível internacional especilista" +
                " em renegociações de dívidas de pessoas jurídicas com garantias";

        var leitor = new Scanner(System.in);

        System.out.println("Como posso ajudar?");

        var openAIClient = new OpenAIClient();

        while(true) {
            var user = leitor.nextLine();
            List<ChatCompletionChoice> choices = openAIClient.sendRequest(system, user, "gpt-3.5-turbo-0125");

            choices.forEach(c -> System.out.println(c.getMessage().getContent()));

            System.out.println("\nAlgo mais?");
        }
    }

       //"Quais garantias posso dar em uma renegociação?"

}