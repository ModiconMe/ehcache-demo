package edu.modicon.ehcachedemo.web.controller;

import edu.modicon.ehcachedemo.application.service.CustomerService;
import edu.modicon.ehcachedemo.web.dto.CustomerDto;
import edu.modicon.ehcachedemo.web.dto.CustomerRegistrationRequest;
import edu.modicon.ehcachedemo.web.dto.CustomerUpdateRequest;
import edu.modicon.ehcachedemo.web.dto.CustomersRegistrationRequest;
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
