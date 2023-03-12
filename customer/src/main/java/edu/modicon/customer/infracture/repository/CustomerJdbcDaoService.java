package edu.modicon.customer.infracture.repository;

import edu.modicon.customer.domain.dao.CustomerDao;
import edu.modicon.customer.domain.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository(value = "jdbc")
public class CustomerJdbcDaoService implements CustomerDao, InitializingBean {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJdbcDaoService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("jdbc dao created");
    }

    @Override
    public List<Customer> fetchAllCustomer() {
        var sql = "SELECT * FROM customer";
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> fetchCustomerById(Long id) {
        var sql = "SELECT * FROM customer WHERE id = ?";
        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @CachePut(value = "edu.modicon.customer.domain.model.Customer", key = "#result.id")
    @Override
    public Customer insertCustomer(Customer customer) {
        var sql = "INSERT INTO customer (id, name, email, age) VALUES (?, ?, ?, ?)";

        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, 1L);
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getEmail());
            ps.setInt(4, customer.getAge());
            return ps;
        }, keyHolder);

        return customer.withId(keyHolder.getKey().longValue());
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        var sql = "UPDATE customer SET name = ?, email = ?, age = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getId()
        );
        return jdbcTemplate.query("SELECT * FROM customer WHERE id = ?", customerRowMapper, customer.getId())
                .stream()
                .findFirst()
                .orElseThrow();
    }

    @Override
    public boolean existByEmail(String email) {
        var sql = "SELECT count(id) FROM customer WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }
}
