package com.samwanjo41.service;

import com.samwanjo41.model.Customer;
import com.samwanjo41.repository.CustomerDao;
import com.samwanjo41.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao dao;

    public CustomerService(CustomerDao dao) {
        this.dao = dao;
    }

    public Customer save(Customer customer) {
        return dao.save(customer);
    }

    public List<Customer> getAll() {
        return dao.findAll();
    }

    public List<Customer> getByName(String name) {
        return dao.findByName(name);
    }
}