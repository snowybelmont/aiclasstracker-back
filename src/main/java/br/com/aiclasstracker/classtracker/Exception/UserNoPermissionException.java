package br.com.aiclasstracker.classtracker.Exception;

public class UserNoPermissionException extends RuntimeException {
    public UserNoPermissionException() {
        super("O usuário não tem permissão para realizar essa ação");
    }
}
