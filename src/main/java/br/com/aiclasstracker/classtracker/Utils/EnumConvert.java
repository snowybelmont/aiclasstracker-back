package br.com.aiclasstracker.classtracker.Utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Converter
public class EnumConvert implements AttributeConverter<RoleEnum, String> {
    private static final RoleEnum[] ROLE_ENUMS = RoleEnum.values();

    @Override
    public String convertToDatabaseColumn(RoleEnum roleEnum) {
        return roleEnum.getCode();
    }

    @Override
    public RoleEnum convertToEntityAttribute(String code) {
        return Arrays.stream(ROLE_ENUMS)
                .filter(roleEnum -> roleEnum.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código não encontrado no Enum: " + code));
    }
}