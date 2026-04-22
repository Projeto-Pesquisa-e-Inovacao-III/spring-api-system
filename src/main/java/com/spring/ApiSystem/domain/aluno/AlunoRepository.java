package com.spring.ApiSystem.domain.aluno;

import com.spring.ApiSystem.domain.aluno.vo.Cpf;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByCpf( Cpf cpf);

    @Query("""
       SELECT COUNT(DISTINCT a)
         FROM Aluno a
         JOIN produto_contratado pc ON pc.aluno = a
        WHERE pc.situacao = true AND 
        pc.produtoExibicao.tipoProduto = :tipoProduto
       """)
    Integer countAlunosComPlanosAtivos(@Param("tipoProduto") TipoProduto tipoProduto);

    @Query("SELECT u.aluno FROM Usuario u WHERE u.ativo = true")
    Page<Aluno> findAllAtivos(Pageable pageable);

    @Query("SELECT a FROM Aluno a JOIN FETCH a.usuario u WHERE u.email = :email")
    Optional<Aluno> findByEmail(@Param("email") String email);

    @Query("""
        SELECT a
        FROM Aluno a
        JOIN FETCH a.usuario u
        LEFT JOIN FETCH u.roles
        WHERE u.email = :email
          AND u.ativo = true
    """)
    Optional<Aluno> findByEmailWithRoles(@Param("email") String email);

    @Query("""
        SELECT a FROM Aluno a
        JOIN FETCH a.usuario u
        LEFT JOIN FETCH u.roles
        WHERE u.ativo = true
          AND (
            :nome IS NULL OR
            u.nome LIKE CONCAT('%', :nome, '%')
          )
    """)
    Page<Aluno> findAllAtivosContainingNome(String nome, Pageable pageable);

    @Query("""
        SELECT a
        FROM Aluno a
        JOIN FETCH a.usuario u
        LEFT JOIN FETCH u.roles
        WHERE u.id = :id
          AND u.ativo = true
    """)
    Optional<Aluno> findByIdRole(Long id);
}
