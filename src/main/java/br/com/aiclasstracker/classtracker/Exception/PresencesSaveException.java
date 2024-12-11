package br.com.aiclasstracker.classtracker.Exception;

public class PresencesSaveException extends RuntimeException {
    public PresencesSaveException() {
        super("Ocorreu um erro ao tentar salvar as presenças dos alunos");
    }
}
