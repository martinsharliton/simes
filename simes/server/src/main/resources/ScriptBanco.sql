CREATE DATABASE IF NOT EXISTS banco;

GRANT ALL PRIVILEGES ON banco.* TO 'root'@'localhost';

USE banco;

CREATE TABLE IF NOT EXISTS teste_aceitacao(
id_teste_aceitacao INT AUTO_INCREMENT PRIMARY KEY,
id_teste_sistema INT NOT NULL,
cliente_aprovou BOOLEAN NOT NULL,
feedback_cliente VARCHAR(200) NOT NULL,
data_aceite DATE NULL,
responsavel_validacao VARCHAR(50) NOT NULL,
status_teste VARCHAR(1)
);
