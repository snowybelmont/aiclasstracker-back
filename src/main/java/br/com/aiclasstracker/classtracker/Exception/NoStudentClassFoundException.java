package br.com.aiclasstracker.classtracker.Exception;

public class NoStudentClassFoundException extends RuntimeException {
    public NoStudentClassFoundException() {
        super("Não foi encontrada uma classe para o aluno");
    }
}
