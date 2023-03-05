package edu.modicon.ehcachedemo.application.service;

import edu.modicon.ehcachedemo.application.dao.CustomerDao;
import edu.modicon.ehcachedemo.domain.model.Customer;
import edu.modicon.ehcachedemo.web.dto.CustomerDto;
import edu.modicon.ehcachedemo.web.dto.CustomerRegistrationRequest;
import edu.modicon.ehcachedemo.web.dto.CustomerUpdateRequest;
import edu.modicon.ehcachedemo.web.dto.CustomersRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final CustomerDtoMapper customerDtoMapper;

    private final CacheManager cacheManager;

    public List<CustomerDto> getAllCustomers() {
        log.info("fetch customers...");
        return customerDao.fetchAllCustomer().stream().map(customerDtoMapper).toList();
    }

    public void addCustomer(CustomerRegistrationRequest request) {
        if (customerDao.existByEmail(request.getEmail())) {
            throw new IllegalStateException("email already taken");
        }

        Customer customer = Customer.builder()
                .email(request.getEmail())
                .name(request.getName())
                .age(request.getAge())
                .build();
        customerDao.insertCustomer(customer);
    }

    public void addCustomers(CustomersRegistrationRequest request) {
        request.getCustomers().forEach(c -> {
            if (customerDao.existByEmail(c.getEmail())) {
                throw new IllegalStateException("email already taken");
            }
            customerDao.insertCustomer(Customer.builder()
                    .email(c.getEmail())
                    .name(c.getName())
                    .age(c.getAge())
                    .build());
        });
    }

    public CustomerDto getCustomer(Long id) {
        log.info("available caches: " + cacheManager.getCacheNames());
        return customerDao.fetchCustomerById(id).map(customerDtoMapper).orElseGet(null);
    }

    public void updateCustomer(CustomerUpdateRequest request) {
        Customer customer = customerDao.fetchCustomerById(request.getId())
                .orElseThrow(() -> new IllegalStateException("customer not found"));

        Customer updateCustomer = customer.toBuilder()
                .name(request.getName() != null ? request.getName() : customer.getName())
                .email(request.getEmail() != null ? request.getEmail() : customer.getEmail())
                .age(request.getAge() != null ? request.getAge() : customer.getAge())
                .build();

        customerDao.updateCustomer(updateCustomer);
    }

}
