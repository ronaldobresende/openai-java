package br.com.resende.features;
import br.com.resende.client.OpenAIClient;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;

import java.nio.file.Path;
import java.util.List;

import static br.com.resende.util.ContadorDeTokens.contarTokens;
import static br.com.resende.util.FileUtil.lerConteudoDoArquivo;

public class IdentificadorDePerfil {

    public static void main(String[] args) {
        var openAIClient = new OpenAIClient();
        var modelo = "gpt-3.5-turbo";

        var tamanhoRespostaEsperada = 2048;

        var promptSistema = """
                Identifique o perfil de compra de cada cliente.
                
                A resposta deve ser:
                
                Cliente - descreva o perfil do cliente em três palavras
                """;

        var arquivo = Path.of("src/main/resources/compras/lista_de_compras_10_clientes.csv");

        var clientes = lerConteudoDoArquivo(arquivo);

        var quantidadeTokens = contarTokens(clientes);

        if (quantidadeTokens > 4096 - tamanhoRespostaEsperada) {
            modelo = "gpt-3.5-turbo-16k";
        }

        System.out.println("QTD TOKENS: " +quantidadeTokens);
        System.out.println("Modelo escolhido: " +modelo);

        openAIClient.setMaxTokens(tamanhoRespostaEsperada);
        openAIClient.setTimeout(60);

        List<ChatCompletionChoice> choices = openAIClient.sendRequest(promptSistema, clientes, modelo);

        System.out.println(choices.get(0).getMessage().getContent());
    }

}
