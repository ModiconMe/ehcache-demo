package edu.modicon.customer.repository;

import edu.modicon.customer.domain.model.Customer;
import edu.modicon.customer.infracture.repository.CustomerJdbcDaoService;
import edu.modicon.customer.infracture.repository.CustomerRowMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJdbcDaoServiceTest extends JdbcTestConfig {

    private final CustomerJdbcDaoService customerJdbcDaoService;

    @Autowired
    public CustomerJdbcDaoServiceTest(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.customerJdbcDaoService = new CustomerJdbcDaoService(getJdbcTemplate(), new CustomerRowMapper());
    }

    @Test
    void shouldReturnAllCustomer() {
        List<Customer> actual = customerJdbcDaoService.fetchAllCustomer();
        assertEquals(5, actual.size());
    }

    @Test
    void shouldReturnCustomerById() {
        Optional<Customer> actual = customerJdbcDaoService.fetchCustomerById(1L);
        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
    }

    @Test
    void shouldNotReturnCustomerById() {
        Optional<Customer> actual = customerJdbcDaoService.fetchCustomerById(100L);
        assertFalse(actual.isPresent());
    }

    @Test
    void shouldReturnTrueIfCustomerExist() {
        assertTrue(customerJdbcDaoService.existByEmail("email"));
    }

    @Test
    void shouldReturnFalseIfCustomerDoNotExist() {
        assertFalse(customerJdbcDaoService.existByEmail("not existed"));
    }

    @Test
    void shouldInsertCustomer() {
        Customer customer = Customer.builder()
                .name("insert_name")
                .email("insert_email")
                .age(100)
                .build();
        assertFalse(customerJdbcDaoService.existByEmail(customer.getEmail()));
        customerJdbcDaoService.insertCustomer(customer);
        assertTrue(customerJdbcDaoService.existByEmail(customer.getEmail()));
    }

    @Test
    void shouldUpdateCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("updated_name")
                .email("updated_email")
                .age(100)
                .build();
        assertFalse(customerJdbcDaoService.existByEmail(customer.getEmail()));
        customerJdbcDaoService.updateCustomer(customer);
        assertTrue(customerJdbcDaoService.existByEmail(customer.getEmail()));
    }

    @Test
    void shouldDeleteCustomerById() {
        customerJdbcDaoService.deleteCustomerById(1L);
    }
}