package com.spring.ApiSystem.domain.alunoExperimental;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.vo.Cpf;
import com.spring.ApiSystem.domain.anamnese.Anamnese;
import com.spring.ApiSystem.domain.usuario.Usuario;
import jakarta.persistence.*;
@Entity
@DiscriminatorValue("EXPERIMENTAL")
public class AlunoExperimental extends Aluno {
    public AlunoExperimental() {
        super();
    }

    public AlunoExperimental(Usuario usuario, Cpf cpf, boolean ativoAnamnese, Anamnese anamnese, boolean profileAtivo) {
        super(null, usuario, cpf, ativoAnamnese, anamnese, profileAtivo);
    }
}
