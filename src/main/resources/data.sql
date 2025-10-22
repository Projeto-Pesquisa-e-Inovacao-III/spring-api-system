-- Alunos
INSERT INTO usuario (nome, email, senha, DTYPE, ativo, cpf) VALUES ('Lucas Andrade', 'lucas.andrade@email.com', 'lucas123', 'Aluno', true, '12345678901');
INSERT INTO usuario (nome, email, senha, DTYPE, ativo, cpf) VALUES ('Mariana Teixeira', 'mariana.teixeira@email.com', 'mari456', 'Aluno', false, '23456789012');
INSERT INTO usuario (nome, email, senha, DTYPE, ativo, cpf) VALUES ('Rafael Cunha', 'rafael.cunha@email.com', 'rafa789', 'Aluno', true, '34567890123');
INSERT INTO usuario (nome, email, senha, DTYPE, ativo, cpf) VALUES ('Juliana Lopes', 'juliana.lopes@email.com', 'ju321', 'Aluno', true, '45678901234');
INSERT INTO usuario (nome, email, senha, DTYPE, ativo, cpf) VALUES ('Thiago Nunes', 'thiago.nunes@email.com', 'thi654', 'Aluno', false, '56789012345');

-- Personais
INSERT INTO usuario (nome, email, senha, DTYPE, ativo, cref) VALUES ('Patrícia Moraes', 'patricia.moraes@email.com', 'pat987', 'Personal', true, 'CREF123456');
INSERT INTO usuario (nome, email, senha, DTYPE, ativo, cref) VALUES ('Rodrigo Pires', 'rodrigo.pires@email.com', 'rod159', 'Personal', true, 'CREF234567');
INSERT INTO usuario (nome, email, senha, DTYPE, ativo, cref) VALUES ('Fernanda Dias', 'fernanda.dias@email.com', 'fer753', 'Personal', false, 'CREF345678');
INSERT INTO usuario (nome, email, senha, DTYPE, ativo, cref) VALUES ('Marcelo Tavares', 'marcelo.tavares@email.com', 'mar852', 'Personal', true, 'CREF456789');
INSERT INTO usuario (nome, email, senha, DTYPE, ativo, cref) VALUES ('Vanessa Ribeiro', 'vanessa.ribeiro@email.com', 'van951', 'Personal', false, 'CREF567890');

ALTER TABLE usuario ALTER COLUMN id RESTART WITH 11;