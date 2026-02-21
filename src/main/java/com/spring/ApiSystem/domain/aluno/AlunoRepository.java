package com.spring.ApiSystem.domain.aluno;

import com.spring.ApiSystem.domain.usuario.UsuarioBaseRepository;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
        pc.produtoExibicao.tipoProduto = :tipoProduto
       """)
    Integer countAlunosComPlanosAtivos(@Param("tipoProduto") TipoProduto tipoProduto);

}
