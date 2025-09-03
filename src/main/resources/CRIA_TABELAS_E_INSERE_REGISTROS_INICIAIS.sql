-- 1. Cria a tabela de empresas e insere dados
CREATE TABLE funcionario (rowid bigint auto_increment, nm_funcionario VARCHAR(255));

INSERT INTO funcionario (nm_funcionario) VALUES ('João'), ('Maria'), ('José'), ('Joana');

-- 2. Cria a tabela de empresas e insere dados
CREATE TABLE empresas (codigo INT PRIMARY KEY AUTO_INCREMENT, nome_empresa VARCHAR(100),
  periodo VARCHAR(100));
  
INSERT INTO empresas (nome_empresa, periodo) VALUES ('Clínica Duomed', 'Manhã');
INSERT INTO empresas (nome_empresa, periodo) VALUES ('Clinica Saude', 'Ambos');
INSERT INTO empresas (nome_empresa, periodo) VALUES ('Clínica Santo Predio', 'Tarde');

-- 3. Cria a tabela de exames e insere dados
CREATE TABLE exames (id INT PRIMARY KEY AUTO_INCREMENT, nome_exame VARCHAR(100));

INSERT INTO exames (nome_exame) VALUES ('Admissional');
INSERT INTO exames (nome_exame) VALUES ('Demissional');
INSERT INTO exames (nome_exame) VALUES ('Periódico');
INSERT INTO exames (nome_exame) VALUES ('Retorno ao Trabalho');
INSERT INTO exames (nome_exame) VALUES ('Mudança de Riscos Ocupacionais');

-- 4. Cria a tabela de ligação entre empresas e exames
CREATE TABLE empresas_exames (empresa_codigo INT, exame_id INT, PRIMARY KEY (empresa_codigo, exame_id),
  FOREIGN KEY (empresa_codigo) REFERENCES empresas(codigo), FOREIGN KEY (exame_id) REFERENCES exames(id));

-- Associa as empresas aos exames (exemplo de dados de ligação)
-- Clínica Saúde (código 1) faz Acuidade Visual (id 1) e Audiometria (id 2)
INSERT INTO empresas_exames (empresa_codigo, exame_id) VALUES (1, 1);
INSERT INTO empresas_exames (empresa_codigo, exame_id) VALUES (1, 2);

-- Clínica Duomed (código 2) faz todos os exames
INSERT INTO empresas_exames (empresa_codigo, exame_id) VALUES (2, 1);
INSERT INTO empresas_exames (empresa_codigo, exame_id) VALUES (2, 2);
INSERT INTO empresas_exames (empresa_codigo, exame_id) VALUES (2, 3);
INSERT INTO empresas_exames (empresa_codigo, exame_id) VALUES (2, 4);
INSERT INTO empresas_exames (empresa_codigo, exame_id) VALUES (2, 5);
  
-- Clínica Santo Prédio (código 3) faz apenas Mudança de Riscos Ocupacionais
INSERT INTO empresas_exames (empresa_codigo, exame_id) VALUES (3, 5);
  
  