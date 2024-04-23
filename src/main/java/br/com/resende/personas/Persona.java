package br.com.resende.personas;

public class Persona {
    private String nome;
    private int idade;
    private String interesse;
    private String localizacao;
    private String profissao;
    private String nivelExperiencia;
    private String idiomaPreferido;
    private String preferenciasComunicacao;
    private String objetivos;
    private String historicoInteracoes;
    private String contextoPessoal;

    public String gerarTextoPersona() {
        return "Você é um " + profissao + " no " + localizacao + ", de nível internacional "  + nivelExperiencia +
                "em " + interesse;
    }

}
