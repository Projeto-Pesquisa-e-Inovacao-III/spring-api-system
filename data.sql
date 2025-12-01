-- ==============================================================================
-- Script SQL para popular dados de teste para os endpoints:
-- 1. /produtos-contratados/planos-vendidos/{quantidadeDias}
-- 2. /produtos-contratados/ganhos-mes/{quantidadeMeses}
-- 3. /agendamentos/consultoria-realizadas/{quantidadeMeses}
-- ==============================================================================

-- Limpar dados existentes (opcional - comente se quiser manter dados existentes)
-- DELETE FROM agendamento;
-- DELETE FROM produto_contratado;
-- DELETE FROM endereco;
-- DELETE FROM cep;
-- DELETE FROM aluno;
-- DELETE FROM personal;
-- DELETE FROM produto_exibicao;
-- DELETE FROM usuario;

-- ==============================================================================
-- 1. INSERIR CEPs
-- ==============================================================================
-- CORRIGIDO: O id é o próprio CEP (String), usa 'localidade' não 'cidade'
INSERT INTO cep (id, logradouro, bairro, localidade, uf) VALUES
('01310-100', 'Avenida Paulista', 'Bela Vista', 'São Paulo', 'SP'),
('22640-100', 'Avenida Atlântica', 'Copacabana', 'Rio de Janeiro', 'RJ'),
('30130-100', 'Avenida Afonso Pena', 'Centro', 'Belo Horizonte', 'MG');

-- ==============================================================================
-- 2. INSERIR USUÁRIOS BASE (Tabela pai)
-- ==============================================================================
-- Senha: "senha123" com salt fictício e hash fictício
-- CORRIGIDO: Removidos IDs fixos (auto-increment)
INSERT INTO usuario (tipo, nome, sexo, data_nascimento, email, salt, senha_hash, ativo, caminho_foto) VALUES
('PERSONAL', 'Carlos Personal', 'M', '1990-05-15', 'personal@example.com', 'salt123', '$argon2id$v=19$m=65536,t=3,p=1$hash123', true, null),
('ALUNO', 'João Silva', 'M', '1995-03-20', 'joao@example.com', 'salt456', '$argon2id$v=19$m=65536,t=3,p=1$hash456', true, null),
('ALUNO', 'Maria Santos', 'F', '1998-07-10', 'maria@example.com', 'salt789', '$argon2id$v=19$m=65536,t=3,p=1$hash789', true, null),
('ALUNO', 'Pedro Oliveira', 'M', '1992-11-25', 'pedro@example.com', 'salt012', '$argon2id$v=19$m=65536,t=3,p=1$hash012', true, null),
('ALUNO', 'Ana Costa', 'F', '1996-01-08', 'ana@example.com', 'salt345', '$argon2id$v=19$m=65536,t=3,p=1$hash345', true, null);

-- ==============================================================================
-- 3. INSERIR PERSONAL
-- ==============================================================================
-- Busca o ID do usuário 'personal@example.com' para inserir na tabela personal
INSERT INTO personal (id, cref, buffer_minutos)
SELECT id, 'CREF-123456', 30 FROM usuario WHERE email = 'personal@example.com';

-- ==============================================================================
-- 4. INSERIR ALUNOS
-- ==============================================================================
-- Busca os IDs dos usuários alunos para inserir na tabela aluno
INSERT INTO aluno (id, cpf)
SELECT id, '12345678901' FROM usuario WHERE email = 'joao@example.com'
UNION ALL
SELECT id, '23456789012' FROM usuario WHERE email = 'maria@example.com'
UNION ALL
SELECT id, '34567890123' FROM usuario WHERE email = 'pedro@example.com'
UNION ALL
SELECT id, '45678901234' FROM usuario WHERE email = 'ana@example.com';

-- ==============================================================================
-- 5. INSERIR ENDEREÇOS
-- ==============================================================================
-- CORRIGIDO: cep_id referencia String (CEP completo), usuario_id busca por email
INSERT INTO endereco (numero, complemento, unidade, tipo, data_criacao, data_atualizacao, usuario_id, cep_id)
SELECT '1500', 'Apto 101', null, 'Residencial', '2024-01-01 10:00:00', null, id, '01310-100' FROM usuario WHERE email = 'joao@example.com'
UNION ALL
SELECT '200', 'Casa', null, 'Residencial', '2024-01-05 11:00:00', null, id, '22640-100' FROM usuario WHERE email = 'maria@example.com'
UNION ALL
SELECT '300', 'Apto 502', null, 'Residencial', '2024-01-10 12:00:00', null, id, '30130-100' FROM usuario WHERE email = 'pedro@example.com'
UNION ALL
SELECT '400', null, null, 'Residencial', '2024-01-15 13:00:00', null, id, '01310-100' FROM usuario WHERE email = 'ana@example.com';

-- ==============================================================================
-- 6. INSERIR PRODUTOS DE EXIBIÇÃO (Planos)
-- ==============================================================================
-- CORRIGIDO: Sem ID (auto-increment), "duracaoMes" com aspas para preservar case, tipo_produto = PACOTE/ADICIONAL, tipo_aula = PRESENCIAL/RESIDENCIAL/FUNCIONAL
INSERT INTO produto_exibicao (titulo, subtitulo, descricao, preco, periodo, status, tipo_produto, data_criacao, tipo_aula, quantidade_aula, duracao_mes) VALUES
('Plano Básico Presencial', '8 aulas mensais', 'Plano básico com 8 aulas presenciais por mês', 350.00, 'Mensal', 'ATIVO', 'PACOTE', '2024-01-01 10:00:00', 'PRESENCIAL', 8, 1),
('Plano Premium Residencial', '12 aulas mensais', 'Plano premium com 12 aulas residenciais por mês', 500.00, 'Mensal', 'ATIVO', 'PACOTE', '2024-01-01 10:00:00', 'RESIDENCIAL', 12, 1),
('Plano Funcional', '8 aulas mensais', 'Plano com 8 aulas funcionais por mês', 250.00, 'Mensal', 'ATIVO', 'PACOTE', '2024-01-01 10:00:00', 'FUNCIONAL', 8, 1),
('Plano Trimestral Presencial', '24 aulas', 'Plano trimestral com 24 aulas presenciais', 900.00, 'Trimestral', 'ATIVO', 'PACOTE', '2024-01-01 10:00:00', 'PRESENCIAL', 24, 3),
('Consultoria Única', 'Sessão única', 'Consultoria presencial por sessão', 150.00, 'Único', 'ATIVO', 'ADICIONAL', '2024-01-01 10:00:00', 'PRESENCIAL', 1, 1);

-- ==============================================================================
-- 7. INSERIR PRODUTOS CONTRATADOS (Distribuídos nos últimos 4 meses)
-- ==============================================================================
-- Data atual é 29/11/2025, então vamos inserir dados de Agosto, Setembro, Outubro e Novembro
-- CORRIGIDO: Sem IDs fixos, usa subconsultas para buscar usuario_aluno_id e produto_exibicao_id

-- NOVEMBRO 2025 (6 vendas - últimos 7 dias e mais antigas)
INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-11-29', '2025-12-29', 8, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'joao@example.com' AND p.titulo = 'Plano Básico Presencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-11-28', '2025-12-28', 12, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'maria@example.com' AND p.titulo = 'Plano Premium Residencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-11-27', '2025-12-27', 8, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'pedro@example.com' AND p.titulo = 'Plano Básico Presencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-11-25', '2025-12-25', 8, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'ana@example.com' AND p.titulo = 'Plano Funcional';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-11-15', '2025-12-15', 12, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'joao@example.com' AND p.titulo = 'Plano Premium Residencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-11-10', '2025-12-10', 8, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'maria@example.com' AND p.titulo = 'Plano Básico Presencial';

-- OUTUBRO 2025 (5 vendas)
INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-10-28', '2025-11-28', 5, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'pedro@example.com' AND p.titulo = 'Plano Básico Presencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-10-20', '2025-11-20', 10, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'ana@example.com' AND p.titulo = 'Plano Premium Residencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-10-15', '2026-01-15', 20, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'joao@example.com' AND p.titulo = 'Plano Trimestral Presencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-10-10', '2025-11-10', 6, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'maria@example.com' AND p.titulo = 'Plano Básico Presencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-10-05', '2025-11-05', 7, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'pedro@example.com' AND p.titulo = 'Plano Funcional';

-- SETEMBRO 2025 (4 vendas)
INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-09-25', '2025-10-25', 4, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'ana@example.com' AND p.titulo = 'Plano Básico Presencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-09-18', '2025-10-18', 8, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'joao@example.com' AND p.titulo = 'Plano Premium Residencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-09-10', '2025-10-10', 5, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'maria@example.com' AND p.titulo = 'Plano Básico Presencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT true, '2025-09-05', '2025-10-05', 6, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'pedro@example.com' AND p.titulo = 'Plano Funcional';

-- AGOSTO 2025 (3 vendas)
INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT false, '2025-08-20', '2025-09-20', 0, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'ana@example.com' AND p.titulo = 'Plano Básico Presencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT false, '2025-08-15', '2025-09-15', 0, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'joao@example.com' AND p.titulo = 'Plano Premium Residencial';

INSERT INTO produto_contratado (situacao, data_compra, data_expiracao, saldo_aula, usuario_aluno_id, produto_exibicao_id)
SELECT false, '2025-08-08', '2025-09-08', 0, u.id, p.id FROM usuario u, produto_exibicao p WHERE u.email = 'maria@example.com' AND p.titulo = 'Plano Básico Presencial';

-- ==============================================================================
-- 8. INSERIR AGENDAMENTOS CONCLUÍDOS (Consultorias realizadas nos últimos meses)
-- ==============================================================================
-- NOTA: Agendamentos foram REMOVIDOS desta versão inicial porque requerem IDs
-- dinâmicos de várias tabelas (endereco_id, aluno_id, personal_id, produto_contratado_id).
--
-- ALTERNATIVA 1: Use a API da aplicação para criar agendamentos após inserir os dados acima.
-- ALTERNATIVA 2: Execute a query abaixo APÓS os dados acima estarem inseridos:

/*
-- Exemplo de INSERT dinâmico para AGENDAMENTO (execute APÓS todas as tabelas acima):
INSERT INTO agendamento (data, data_fim, status, descricao, endereco_id, aluno_id, personal_id, produto_contratado_id)
SELECT
    '2025-11-28 09:00:00',
    '2025-11-28 10:00:00',
    'CONCLUIDO',
    'Consultoria de avaliação física',
    e.id,
    u.id,
    p.id,
    pc.id
FROM
    endereco e,
    usuario u,
    usuario p,
    produto_contratado pc
WHERE
    u.email = 'joao@example.com'
    AND p.email = 'personal@example.com'
    AND e.usuario_id = u.id
    AND pc.usuario_aluno_id = u.id
    AND pc.data_compra = '2025-11-29'
LIMIT 1;

-- Repita o padrão acima para cada agendamento, ajustando:
-- - As datas (data e data_fim)
-- - O status
-- - A descrição
-- - O email do aluno (u.email)
-- - A data_compra do produto_contratado para identificar o correto
*/

-- ==============================================================================
-- RESUMO DOS DADOS INSERIDOS:
-- ==============================================================================
-- CORREÇÕES APLICADAS:
-- ✅ Todos os IDs fixos REMOVIDOS (evita conflito de Primary Key)
-- ✅ CEP: id é String (CEP completo), usa 'localidade' não 'cidade'
-- ✅ ENDERECO: cep_id referencia String, usuario_id usa subconsulta
-- ✅ PRODUTO_EXIBICAO: duracaoMes (camelCase)
-- ✅ PRODUTO_EXIBICAO: tipo_produto = PACOTE/ADICIONAL (não PLANO/CONSULTORIA)
-- ✅ PRODUTO_EXIBICAO: tipo_aula = PRESENCIAL/RESIDENCIAL/FUNCIONAL (não INDIVIDUAL/DUPLA)
-- ✅ PERSONAL e ALUNO: usam subconsultas para buscar ID do usuário
-- ✅ PRODUTO_CONTRATADO: usa subconsultas para buscar IDs de aluno e produto
-- ⚠️  AGENDAMENTOS: REMOVIDOS (use a API para criar, ou veja exemplo comentado)
--
-- DADOS INSERIDOS:
-- - 1 Personal (Carlos - email: personal@example.com)
-- - 4 Alunos (João, Maria, Pedro, Ana)
-- - 3 CEPs
-- - 4 Endereços
-- - 5 Produtos de Exibição (Planos)
-- - 18 Produtos Contratados:
--   * Novembro 2025: 6 vendas (incluindo últimos 7 dias: 4 vendas)
--   * Outubro 2025: 5 vendas
--   * Setembro 2025: 4 vendas
--   * Agosto 2025: 3 vendas
--
-- GANHOS MENSAIS ESPERADOS (soma dos preços dos produtos):
-- - Novembro 2025: 350 + 500 + 350 + 250 + 500 + 350 = 2.300,00
-- - Outubro 2025: 350 + 500 + 900 + 350 + 250 = 2.350,00
-- - Setembro 2025: 350 + 500 + 350 + 250 = 1.450,00
-- - Agosto 2025: 350 + 500 + 350 = 1.200,00
--
-- PLANOS VENDIDOS ÚLTIMOS 7 DIAS: 4
--
-- ENDPOINTS FUNCIONAIS:
-- ✅ GET /produtos-contratados/planos-vendidos/{dias} - Funcionará!
-- ✅ GET /produtos-contratados/ganhos-mes/{meses} - Funcionará!
-- ⚠️  GET /agendamentos/consultoria-realizadas/{meses} - Não terá dados (crie via API)
-- ==============================================================================

-- Para verificar os dados inseridos, execute:
-- SELECT * FROM usuario;
-- SELECT * FROM produto_exibicao;
-- SELECT * FROM produto_contratado ORDER BY data_compra DESC;
-- SELECT u.email, pc.data_compra, pe.titulo, pc.saldo_aula
-- FROM produto_contratado pc
-- JOIN usuario u ON pc.usuario_aluno_id = u.id
-- JOIN produto_exibicao pe ON pc.produto_exibicao_id = pe.id
-- ORDER BY pc.data_compra DESC;

