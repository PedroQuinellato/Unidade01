-- ============================================================
--  Exercício 3 – Script SQL para PostgreSQL
--  Execute este script antes de rodar a aplicação Java.
-- ============================================================

-- 1. Crie o banco de dados (execute conectado ao banco 'postgres')
-- CREATE DATABASE exercicio3;

-- 2. Conecte ao banco e execute o restante:

-- Tabela produto
CREATE TABLE IF NOT EXISTS produto (
    id         SERIAL PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    descricao  VARCHAR(255),
    preco      NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    quantidade INTEGER NOT NULL DEFAULT 0
);

-- Dados de exemplo para testar
INSERT INTO produto (nome, descricao, preco, quantidade) VALUES
    ('Notebook Dell Inspiron', 'Processador Intel i5, 8GB RAM, 256GB SSD', 3499.90, 15),
    ('Mouse Logitech MX Master', 'Mouse sem fio ergonômico com scroll horizontal', 399.00, 42),
    ('Teclado Mecânico Redragon', 'Switch Blue, RGB, layout ABNT2', 289.90, 30),
    ('Monitor LG 24"', 'Full HD, IPS, 75Hz, borda fina', 1199.00, 8),
    ('Headset HyperX Cloud II', 'Surround 7.1, drivers 53mm, microfone destacável', 549.90, 20);

-- Conferindo os dados inseridos
SELECT * FROM produto ORDER BY id;
