create user test_user with password 'qwerty';
create database test_db;
grant all privileges on database test_db to test_user;
\connect test_db test_user;
create schema if not exists test_schema authorization test_user;

-- Создаем таблицу
CREATE TABLE test_schema.students (
    student_id INTEGER,
    student_name VARCHAR(100),
    faculty VARCHAR(100)
);