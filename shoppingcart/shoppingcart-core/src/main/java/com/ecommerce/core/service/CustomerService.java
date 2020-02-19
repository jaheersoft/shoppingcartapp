package com.ecommerce.core.service;

import com.ecommerce.core.entities.Customer;
import com.ecommerce.core.entities.Order;
import com.ecommerce.core.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Siva
 *
 */
@Service
@Transactional
public class CustomerService {
    private CustomerRepository customerRepository;

    @Autowired
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
	}

	public Customer createCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public Customer getCustomerById(Integer id) {
		return customerRepository.getOne(id);
	}

	public List<Order> getCustomerOrders(String customerEmail) {
		return customerRepository.getCustomerOrders(customerEmail);
	}

}
