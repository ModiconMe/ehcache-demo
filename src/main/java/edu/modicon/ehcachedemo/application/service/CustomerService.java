package edu.modicon.ehcachedemo.application.service;

import edu.modicon.ehcachedemo.application.dao.CustomerDao;
import edu.modicon.ehcachedemo.domain.model.Customer;
import edu.modicon.ehcachedemo.web.dto.CustomerDto;
import edu.modicon.ehcachedemo.web.dto.CustomerRegistrationRequest;
import edu.modicon.ehcachedemo.web.dto.CustomersRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final CustomerDtoMapper customerDtoMapper;

    private final SessionFactory sessionFactory;

    public List<CustomerDto> getAllCustomers() {
        log.info("fetch customers...");
        log.info("L2 cache hits: " + sessionFactory.getStatistics().getSecondLevelCacheHitCount());
        log.info("L2 cache puts: " + sessionFactory.getStatistics().getSecondLevelCachePutCount());
        log.info("L1 cache puts: " + sessionFactory.getStatistics().getQueryCacheHitCount());
        log.info("L1 cache puts: " + sessionFactory.getStatistics().getQueryCachePutCount());
        log.info("L2 cache region names: " + Arrays.toString(sessionFactory.getStatistics().getSecondLevelCacheRegionNames()));
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
            customerDao.insertCustomer(Customer.builder()
                    .email(c.getEmail())
                    .name(c.getName())
                    .age(c.getAge())
                    .build());
        });
    }

    public CustomerDto getCustomer(String id) {
        Statistics statistics = sessionFactory.getStatistics();
        log.info("L2 cache hits: " + statistics.getSecondLevelCacheHitCount());
        log.info("L2 cache puts: " + statistics.getSecondLevelCachePutCount());
        log.info("L1 cache puts: " + statistics.getQueryCacheHitCount());
        log.info("L1 cache puts: " + statistics.getQueryCachePutCount());
        log.info("L2 cache region names: " + Arrays.toString(statistics.getSecondLevelCacheRegionNames()));
        log.info("L2 cache puts: " + statistics.getEntityStatistics("edu.modicon.ehcachedemo.domain.model.Customer"));
        return customerDao.fetchCustomerById(Long.parseLong(id)).map(customerDtoMapper).orElseGet(null);
    }

}
