package br.com.resende.util;

public class ApiKeyUtil {

    public static String getToken(){
        return System.getenv("OPENAI_API_KEY");
    }
}
