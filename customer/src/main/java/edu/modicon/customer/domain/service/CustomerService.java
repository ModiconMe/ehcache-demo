package edu.modicon.customer.domain.service;

import edu.modicon.customer.domain.dao.CustomerDao;
import edu.modicon.customer.domain.model.Customer;
import edu.modicon.customer.application.dto.CustomerDto;
import edu.modicon.customer.application.dto.CustomerRegistrationRequest;
import edu.modicon.customer.application.dto.CustomerUpdateRequest;
import edu.modicon.customer.application.dto.CustomersRegistrationRequest;
import edu.modicon.customer.domain.service.CustomerDtoMapper;
import edu.modicon.customer.infracture.queue.JmsProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final CustomerDtoMapper customerDtoMapper;

    private final CacheManager cacheManager;
    private final JmsProducer jmsProducer;

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

        jmsProducer.sendMessageToQueue(customer);
        jmsProducer.sendMessageToTopic(customer);
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
