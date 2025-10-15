INSERT INTO usuario (nome, sexo, data_nascimento, email, senha, ativo) VALUES
('Ana Souza', 'F', '1990-05-12', 'ana.souza@email.com', 'senha123', true),
('Carlos Lima', 'M', '1985-09-30', 'carlos.lima@email.com', 'segredo456', true),
('Beatriz Mendes', 'F', '1992-03-18', 'beatriz.m@email.com', 'abc123', true),
('João Pedro', 'M', '1988-11-22', 'joao.pedro@email.com', 'joaop@2023', true),
('Fernanda Rocha', 'F', '1995-07-07', 'fernanda.rocha@email.com', 'f3rn@nda', true),
('Lucas Martins', 'M', '1993-01-15', 'lucas.martins@email.com', 'lucasM!', true),
('Mariana Silva', 'F', '1991-04-25', 'mariana.silva@email.com', 'm@r1ana', true),
('Rafael Torres', 'M', '1987-08-19', 'rafael.torres@email.com', 'r@f@elT', true),
('Juliana Costa', 'F', '1994-12-03', 'juliana.costa@email.com', 'julianaC#', true),
('Eduardo Alves', 'M', '1989-06-10', 'eduardo.alves@email.com', 'edualves$', true);

ALTER TABLE usuario ALTER COLUMN id RESTART WITH 11;