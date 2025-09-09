-- 1. Cria a tabela de funcionario e insere alguns registros iniciais.
CREATE TABLE funcionario (
    rowid bigint auto_increment PRIMARY KEY,
    nm_funcionario VARCHAR(255)
);

INSERT INTO funcionario (nm_funcionario) VALUES ('João'), ('Maria'), ('José'), ('Joana');

-- 2. Cria a tabela agenda e insere alguns registros iniciais.
CREATE TABLE agenda (
  codigo INT PRIMARY KEY AUTO_INCREMENT,
  nm_agenda VARCHAR(100),
  periodo VARCHAR(100)
);

INSERT INTO agenda (nm_agenda, periodo) VALUES ('Agenda Clínica Duomed', 'Manhã');
INSERT INTO agenda (nm_agenda, periodo) VALUES ('Agenda Clinica Saude', 'Ambos');
INSERT INTO agenda (nm_agenda, periodo) VALUES ('Agenda Clínica Santo Predio', 'Tarde');

-- 3. Cria a tabela agenda com exames já pré-definidos.
CREATE TABLE exame (
    id INT PRIMARY KEY AUTO_INCREMENT, 
    nm_exame VARCHAR(100)
);

INSERT INTO exame (nm_exame) VALUES ('Admissional'), ('Demissional'), ('Periódico'), ('Retorno ao Trabalho'), ('Mudança de Riscos Ocupacionais');

-- 4. Cria a tabela de relacionamento entre 'agenda' e 'exame' (agenda_exames).
-- E insere as associações iniciais entre agendas e exames.
CREATE TABLE agenda_exames (
  agenda_codigo INT,
  exame_id INT,
  PRIMARY KEY (agenda_codigo, exame_id),
  FOREIGN KEY (agenda_codigo) REFERENCES agenda(codigo),
  FOREIGN KEY (exame_id) REFERENCES exame(id)
);

INSERT INTO agenda_exames (agenda_codigo, exame_id) VALUES (1, 1), (1, 2);
INSERT INTO agenda_exames (agenda_codigo, exame_id) VALUES (2, 1), (2, 2), (2, 3), (2, 4), (2, 5);
INSERT INTO agenda_exames (agenda_codigo, exame_id) VALUES (3, 5);

--5. Cria a tabela de 'compromisso' para agendamentos e insere os compromissos como exemplo.
CREATE TABLE compromisso (
  codigo INT PRIMARY KEY AUTO_INCREMENT,
  id_funcionario BIGINT NOT NULL,
  id_agenda INT NOT NULL,
  data_compromisso DATE NOT NULL,
  hora_compromisso TIME NOT NULL,
  FOREIGN KEY (id_funcionario) REFERENCES funcionario(rowid),
  FOREIGN KEY (id_agenda) REFERENCES agenda(codigo)
);

INSERT INTO compromisso (id_funcionario, id_agenda, data_compromisso, hora_compromisso) VALUES (4, 1, '2025-09-10', '08:00:00'), (3, 1, '2025-09-10', '08:30:00'), (2, 1, '2025-09-10', '09:00:00'), (1, 1, '2025-09-10', '09:30:00'), (4, 1, '2025-09-10', '10:00:00'), (2, 1, '2025-09-10', '10:30:00'), (3, 1, '2025-09-10', '11:00:00'), (1, 1, '2025-09-10', '11:30:00');
INSERT INTO compromisso (id_funcionario, id_agenda, data_compromisso, hora_compromisso) VALUES (1, 2, '2025-09-15', '08:00:00'), (2, 2, '2025-09-15', '08:30:00'), (3, 2, '2025-09-15', '09:00:00'), (4, 2, '2025-09-15', '09:30:00'), (1, 2, '2025-09-15', '10:00:00'), (2, 2, '2025-09-15', '10:30:00'), (3, 2, '2025-09-15', '11:00:00'), (4, 2, '2025-09-15', '11:30:00'), (1, 2, '2025-09-15', '13:00:00'), (2, 2, '2025-09-15', '13:30:00'), (3, 2, '2025-09-15', '14:00:00'), (4, 2, '2025-09-15', '14:30:00'), (1, 2, '2025-09-15', '15:00:00'), (2, 2, '2025-09-15', '15:30:00'), (3, 2, '2025-09-15', '16:00:00'), (4, 2, '2025-09-15', '16:30:00');
INSERT INTO compromisso (id_funcionario, id_agenda, data_compromisso, hora_compromisso) VALUES (1, 3, '2025-09-12', '13:00:00'), (2, 3, '2025-09-12', '13:30:00'), (3, 3, '2025-09-12', '14:00:00'), (4, 3, '2025-09-12', '14:30:00'), (1, 3, '2025-09-12', '15:00:00'), (2, 3, '2025-09-12', '15:30:00'), (3, 3, '2025-09-12', '16:00:00');

--6. Cria a tabela de relacionamento entre 'compromisso' e 'exames'
CREATE TABLE compromisso_exames (
  compromisso_codigo INT,
  exame_id INT,
  PRIMARY KEY (compromisso_codigo, exame_id),
  FOREIGN KEY (compromisso_codigo) REFERENCES compromisso(codigo),
  FOREIGN KEY (exame_id) REFERENCES exame(id)
);