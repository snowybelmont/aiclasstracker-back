package br.com.aiclasstracker.classtracker.Exception;

public class NoLessonFoundException extends RuntimeException {
    public NoLessonFoundException() {
        super("Nenhuma aula foi encontrada");
    }
}
