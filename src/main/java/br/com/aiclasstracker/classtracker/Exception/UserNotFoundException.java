package br.com.aiclasstracker.classtracker.Exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Não foi possível localizar o usuário solicitado. Verifique se o identificador está correto.");
    }
}