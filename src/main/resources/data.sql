INSERT INTO cep (id, bairro, localidade, logradouro, uf) VALUES
                                                             ('01001-000', 'Sé', 'São Paulo', 'Praça da Sé', 'SP'),
                                                             ('20010-000', 'Centro', 'Rio de Janeiro', 'Rua Primeiro de Março', 'RJ');

INSERT INTO usuario (ativo, data_nascimento, email, nome, salt, senha_hash, sexo, cpf, cref, dtype) VALUES
                                                                                                        (true, PARSEDATETIME('1990-05-15', 'yyyy-MM-dd'), 'aluno1@email.com', 'João Silva', 'salt123', 'hash123', 'M', '123.456.789-00', NULL, 'Aluno'),
                                                                                                        (true, PARSEDATETIME('1985-03-20', 'yyyy-MM-dd'), 'personal1@email.com', 'Maria Souza', 'salt456', 'hash456', 'F', NULL, 'CREF12345', 'Personal');

INSERT INTO endereco (complemento, data_atualizacao, data_criacao, numero, tipo, unidade, cep_id, usuario_id) VALUES
                                                                                                                  ('Apto 101', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '100', 'Residencial', 'Unidade A', '01001-000', 1),
                                                                                                                  ('Casa', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '200', 'Residencial', 'Unidade B', '20010-000', 2);

INSERT INTO telefone (ddd, numero, pais, usuario_id) VALUES
                                                         ('11', '987654321', 'BR', 1),
                                                         ('21', '912345678', 'BR', 2);

INSERT INTO produto_exibicao (data_atualizacao, data_criacao, descricao, duracao_mes, periodo, preco, quantidade_aula, status, subtitulo, tipo_aula, titulo) VALUES
                                                                                                                                                                 (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'Curso de Musculação', 6, 'Mensal', 500.0, 24, 'ATIVO', 'Musculação para iniciantes', 'Presencial', 'Musculação Básica'),
                                                                                                                                                                 (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'Treinamento Funcional', 3, 'Semestral', 300.0, 12, 'ATIVO', 'Funcional avançado', 'Presencial', 'Funcional Avançado');

INSERT INTO produto_contratado (ativo, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id) VALUES
                                                                                                                           (true, CURRENT_TIMESTAMP(), DATEADD('MONTH', 6, CURRENT_TIMESTAMP()), 24, 1, 1),
                                                                                                                           (true, CURRENT_TIMESTAMP(), DATEADD('MONTH', 3, CURRENT_TIMESTAMP()), 12, 1, 2);

INSERT INTO agendamento (data, descricao, situacao, aluno_id, endereco_id, personal_id, produto_contratado_id) VALUES
                                                                                                                   (DATEADD('DAY', 2, CURRENT_TIMESTAMP()), 'Primeira aula de musculação', 0, 1, 1, 2, 1),
                                                                                                                   (DATEADD('DAY', 3, CURRENT_TIMESTAMP()), 'Treino funcional', 0, 1, 1, 2, 2);
