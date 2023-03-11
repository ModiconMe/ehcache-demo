package edu.modicon.customer.repository;

import edu.modicon.customer.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJdbcTest
public class JdbcTestConfig {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTestConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        Customer customer = Customer.builder()
                .name("name")
                .email("email")
                .age(12).build();
        jdbcTemplate.update("INSERT INTO customer (name, email, age) VALUES (?, ?, ?)",
                customer.getName(), customer.getEmail(), customer.getAge());
    }
}
