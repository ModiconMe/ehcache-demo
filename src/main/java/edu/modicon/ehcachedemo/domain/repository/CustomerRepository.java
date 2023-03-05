package edu.modicon.ehcachedemo.domain.repository;

import edu.modicon.ehcachedemo.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
}
