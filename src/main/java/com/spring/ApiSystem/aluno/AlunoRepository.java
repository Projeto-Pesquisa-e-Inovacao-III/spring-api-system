package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.usuario.UsuarioBaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends UsuarioBaseRepository<Aluno> {
    boolean existsByCpf(String cpf);

    List<Aluno> findAllBy(Pageable pageable);

    @Query("""
       SELECT COUNT(DISTINCT a)
         FROM Aluno a
         JOIN produto_contratado pc ON pc.aluno = a
        WHERE pc.situacao = true AND 
        pc.produtoExibicao.tipoProduto = com.spring.ApiSystem.produtoexibicao.enums.TipoProduto.PACOTE
       """)
    Integer countAlunosComPlanosAtivos();

}
