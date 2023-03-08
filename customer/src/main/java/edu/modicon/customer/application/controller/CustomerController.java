package edu.modicon.customer.application.controller;

import edu.modicon.customer.application.dto.CustomerDto;
import edu.modicon.customer.application.dto.CustomerRegistrationRequest;
import edu.modicon.customer.application.dto.CustomerUpdateRequest;
import edu.modicon.customer.application.dto.CustomersRegistrationRequest;
import edu.modicon.customer.domain.service.CustomerService;
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
