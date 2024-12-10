package br.com.aiclasstracker.classtracker.Exception;

public class TokenGenerateException extends RuntimeException {
    public TokenGenerateException() {
        super("Ocorreu um erro ao tentar gerar o token. Por favor, tente novamente.");
    }
}