package ru.alishev.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1234";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public List<Person> index() {
        return jdbcTemplate.query("select * from person", new BeanPropertyRowMapper<>(Person.class));
    }

    //Если есть хотя бы один обьект класса Person то вернёт, иначе вернёт null
    public Person show(int id) {
        return jdbcTemplate.query(
                        "SELECT * FROM person where id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class)
                )
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person VALUES (1,?,?,?)",
                person.getName(),
                person.getAge(),
                person.getEmail());

    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person set name=?, age=?, email=? where id=?",
                updatedPerson.getName(),
                updatedPerson.getAge(),
                updatedPerson.getEmail(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }
}
