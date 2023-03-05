package edu.modicon.ehcachedemo.application.dao;

import edu.modicon.ehcachedemo.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> fetchAllCustomer();
    Optional<Customer> fetchCustomerById(Long id);
    void insertCustomer(Customer customer);
    void deleteCustomerById(Long id);
    void updateCustomer(Customer update);
    boolean existByEmail(String email);
}