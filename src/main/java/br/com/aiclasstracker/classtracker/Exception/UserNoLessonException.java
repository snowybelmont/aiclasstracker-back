package br.com.aiclasstracker.classtracker.Exception;

public class UserNoLessonException extends RuntimeException {
    public UserNoLessonException() {
        super("Nenhuma aula foi encontrada para o usuário");
    }
}
