package com.samwanjo41.controller;

import com.samwanjo41.model.Customer;
import com.samwanjo41.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/create-customer")
    public Customer create(@RequestBody Customer customer) {
        log.info("Received customer: {}", customer);
        return service.save( customer);
    }

    @GetMapping("/customers")
    public List<Customer> getAll() {
        return service.getAll();
    }

    @GetMapping("/{name}")
    public List<Customer> getByName(@PathVariable("name") String name) {
        return service.getByName(name);
    }

}