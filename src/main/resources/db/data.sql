INSERT INTO usuario (ativo, data_nascimento, email, nome, salt, senha_hash, sexo, cpf, cref, tipo) VALUES
                                                                                                       (true, PARSEDATETIME('1990-05-15', 'yyyy-MM-dd'), 'aluno1@email.com', 'João Silva', 'salt123', 'hash123', 'M', '123.456.788-00', NULL, 'Aluno'),
                                                                                                       (true, PARSEDATETIME('1985-03-20', 'yyyy-MM-dd'), 'personal1@email.com', 'Maria Souza', 'salt456', 'hash456', 'F', NULL, 'CREF12345', 'Personal');


INSERT INTO produto_exibicao (data_atualizacao, data_criacao, descricao, duracao_mes, periodo, preco, quantidade_aula, status, subtitulo, tipo_aula, titulo) VALUES
                                                                                                                                                                 (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'Consultoria 6 meses', 6, 'Semestral', 500.0, 24, 'ATIVO', '', 'Presencial', 'Pacote 6 meses'),
                                                                                                                                                                 (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'Consultoria 3 meses', 1, 'Mensal', 300.0, 12, 'ATIVO', '', 'Presencial', 'Pacote 3 meses');

INSERT INTO produto_contratado (ativo, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id) VALUES(true, CURRENT_TIMESTAMP(), DATEADD('MONTH', 6, CURRENT_TIMESTAMP()), 24, 1, 1);