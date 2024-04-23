package br.com.resende.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {

    public static String lerConteudoDoArquivo(Path arquivo) {
        try {
            return Files.readAllLines(arquivo).toString();
        } catch (Exception e) {
            throw new RuntimeException("Falha ler conteudo do arquivo.", e);
        }
    }

    public static List<Path> carregarArquivosDiretorio(String diretorio, String extensao) {
        try {
            var diretorioAvaliacoes = Path.of(diretorio);
            return Files
                    .walk(diretorioAvaliacoes, 1)
                    .filter(path -> path.toString().endsWith(extensao))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Falha ao carregar os arquivos do diretório.", e);
        }
    }

}
