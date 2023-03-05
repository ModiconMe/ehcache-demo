package edu.modicon.ehcachedemo.application.dao;

import edu.modicon.ehcachedemo.domain.model.Customer;
import edu.modicon.ehcachedemo.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CustomerJpaDaoService implements CustomerDao {
    private final CustomerRepository repository;

    @Override
    public List<Customer> fetchAllCustomer() {
        return repository.findAll();
    }

    @Override
    public Optional<Customer> fetchCustomerById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        repository.save(customer);
    }

    @Override
    public void updateCustomer(Customer update) {
        repository.save(update);
    }

    @Override
    public void deleteCustomerById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
