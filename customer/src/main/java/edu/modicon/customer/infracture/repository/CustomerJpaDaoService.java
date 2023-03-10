package edu.modicon.customer.infracture.repository;

import edu.modicon.customer.domain.dao.CustomerDao;
import edu.modicon.customer.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository("jpa")
public class CustomerJpaDaoService implements CustomerDao {
    private final CustomerRepository repository;

    @Override
    public List<Customer> fetchAllCustomer() {
        return repository.findAll();
    }

    @Cacheable(value = "edu.modicon.customer.domain.model.Customer", key = "#id")
    @Override
    public Optional<Customer> fetchCustomerById(Long id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return repository.findById(id);
    }

    @CachePut(value = "edu.modicon.customer.domain.model.Customer", key = "#result.id")
    @Override
    public Customer insertCustomer(Customer customer) {
        return repository.save(customer);
    }

    @CachePut(value = "edu.modicon.customer.domain.model.Customer", key = "#customer.id")
    @Override
    public Customer updateCustomer(Customer customer) {
        return repository.save(customer);
    }

    @CacheEvict(value = "edu.modicon.customer.domain.model.Customer", key = "#id")
    @Override
    public void deleteCustomerById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
