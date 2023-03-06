package edu.modicon.ehcachedemo.domain.dao;

import edu.modicon.ehcachedemo.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> fetchAllCustomer();
    Optional<Customer> fetchCustomerById(Long id);
    Customer insertCustomer(Customer customer);
    void deleteCustomerById(Long id);
    Customer updateCustomer(Customer update);
    boolean existByEmail(String email);
}
