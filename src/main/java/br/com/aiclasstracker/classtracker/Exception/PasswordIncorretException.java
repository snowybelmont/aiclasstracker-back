package br.com.aiclasstracker.classtracker.Exception;

public class PasswordIncorretException extends RuntimeException {
    public PasswordIncorretException() {
        super("O login ou a senha fornecida estão incorretos. Verifique suas credenciais e tente novamente.");
    }
}