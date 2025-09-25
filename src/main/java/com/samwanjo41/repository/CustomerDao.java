package com.samwanjo41.repository;

import com.samwanjo41.model.Customer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDao {

    private final MongoTemplate mongoTemplate;

    public CustomerDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Customer save(Customer customer) {
        return mongoTemplate.save(customer);
    }

    public List<Customer> findAll() {
        return mongoTemplate.findAll(Customer.class);
    }

    public List<Customer> findByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.find(query, Customer.class);
    }
}
