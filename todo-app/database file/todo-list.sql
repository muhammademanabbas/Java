CREATE DATABASE TodoListDatabase ; 
USE TodoListDatabase   ; 
CREATE TABLE tasks (
  id INT PRIMARY KEY AUTO_INCREMENT,  
  title VARCHAR(255) NOT NULL,  
  description VARCHAR(255) NOT NULL
);
drop database TodoListDatabase  ;
