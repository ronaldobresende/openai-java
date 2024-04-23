package br.com.resende.features;

import br.com.resende.client.OpenAIClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static br.com.resende.util.FileUtil.carregarArquivosDiretorio;
import static br.com.resende.util.FileUtil.lerConteudoDoArquivo;

public class AnaliseDeSentimentos {

    public static void main(String[] args) {
        var openAIClient = new OpenAIClient();
        openAIClient.setTimeout(60);

        var arquivosDeAvaliacoes = carregarArquivosDiretorio("src/main/resources/avaliacoes", "txt");

        var promptSistema = """
                Você é um analisador de sentimentos de avaliações de produtos.
                Escreva um parágrafo com até 50 palavras resumindo as avaliações e depois atribua qual o sentimento geral para o produto.
                Identifique também 3 pontos fortes e 3 pontos fracos identificados a partir das avaliações.
                                
                #### Formato de saída
                Nome do produto:
                Resumo das avaliações: [resuma em até 50 palavras]
                Sentimento geral: [deve ser: POSITIVO, NEUTRO ou NEGATIVO]
                Pontos fortes: [3 bullets points]
                Pontos fracos: [3 bullets points]
                """;

        for (var arquivo : arquivosDeAvaliacoes) {
            System.out.println("Iniciando analise do produto: " +arquivo.getFileName());

            var promptUsuario = lerConteudoDoArquivo(arquivo);

            var choices = openAIClient.sendRequest(promptSistema, promptUsuario, "gpt-4");

            var resposta = choices.get(0).getMessage().getContent();
            salvarArquivoDeAnaliseDeSentimento(arquivo, resposta);

            System.out.println("Analise finalizada");
        }

    }
    private static void salvarArquivoDeAnaliseDeSentimento(Path arquivo, String analise) {
        try {
            var nomeProduto = arquivo
                    .getFileName()
                    .toString()
                    .replace(".txt", "")
                    .replace("avaliacoes-", "");
            var path = Path.of("src/main/resources/analises/analise-sentimentos-" +nomeProduto +".txt");
            Files.writeString(path, analise, StandardOpenOption.CREATE_NEW);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o arquivo!", e);
        }
    }

}
