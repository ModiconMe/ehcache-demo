package edu.modicon.ehcachedemo.application.controller;

import edu.modicon.ehcachedemo.application.dto.CustomerDto;
import edu.modicon.ehcachedemo.application.dto.CustomerRegistrationRequest;
import edu.modicon.ehcachedemo.application.dto.CustomerUpdateRequest;
import edu.modicon.ehcachedemo.application.dto.CustomersRegistrationRequest;
import edu.modicon.ehcachedemo.domain.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping
    public void addCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @PostMapping("/all")
    public void addCustomers(@RequestBody CustomersRegistrationRequest request) {
        customerService.addCustomers(request);
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable String id) {
        return customerService.getCustomer(Long.parseLong(id));
    }

    @PutMapping("/{id}")
    public void updateCustomer(@PathVariable String id, @RequestBody CustomerUpdateRequest request) {
        customerService.updateCustomer(request.withId(Long.parseLong(id)));
    }

}
