package br.com.aiclasstracker.classtracker.Exception;

public class NoCallHistoryFoundException extends RuntimeException{
    public NoCallHistoryFoundException() {
        super("Nenhuma chamada foi encontrada");
    }
}
