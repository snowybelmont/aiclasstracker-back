package br.com.aiclasstracker.classtracker.Utils;

import lombok.Getter;

@Getter
public enum RoleEnum {
    STUDENT("A", "Aluno", "ROLE_STUDENT"),
    PROFESSOR("P", "Professor", "ROLE_PROFESSOR"),
    ADMINISTRATOR("C", "Administração", "ROLE_ADMINISTRATOR"),
    DEVELOPER("D", "Desenvolvedor", "ROLE_DEVELOPER");

    private final String code;
    private final String name;
    private final String securityRole;

    RoleEnum(String code, String name, String securityRole) {
        this.code = code;
        this.name = name;
        this.securityRole = securityRole;
    }
}