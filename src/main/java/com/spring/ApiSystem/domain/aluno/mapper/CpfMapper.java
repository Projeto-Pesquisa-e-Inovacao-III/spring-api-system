// java
package com.spring.ApiSystem.domain.aluno.mapper;

import com.spring.ApiSystem.domain.aluno.dto.request.ReqCpfDTO;
import com.spring.ApiSystem.domain.aluno.vo.Cpf;
import com.spring.ApiSystem.domain.aluno.vo.CpfConverter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CpfMapper {

    default Cpf toCpf(String cpf) {
        if (cpf == null) return null;
        var converter = new CpfConverter();
        return converter.convertToEntityAttribute(cpf);
    }

    default Cpf fromReq(ReqCpfDTO dto) {
        if (dto == null) return null;
        return toCpf(dto.value());
    }

    default String toValue(Cpf cpf) {
        return cpf == null ? null : cpf.getValue();
    }
}
