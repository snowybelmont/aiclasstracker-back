package br.com.aiclasstracker.classtracker.Exception;

public class CallSaveException extends RuntimeException {
    public CallSaveException() {
        super("Ocorreu um erro ao tentar salvar a chamada");
    }
}
