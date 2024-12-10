package br.com.aiclasstracker.classtracker.Exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
        super("O token solicitado não foi encontrado. Verifique se o token está correto ou se ele ainda é válido.");
    }
}