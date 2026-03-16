// java
package com.spring.ApiSystem.domain.aluno.mapper;

import com.spring.ApiSystem.domain.aluno.vo.Cpf;
import com.spring.ApiSystem.domain.aluno.vo.CpfConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CpfMapper {

    @Named("toCpf")
    default Cpf toCpf(String cpf) {
        if (cpf == null) return null;
        var converter = new CpfConverter();
        return converter.convertToEntityAttribute(cpf);
    }

    default String toValue(Cpf cpf) {
        return cpf == null ? null : cpf.getValue();
    }
}
