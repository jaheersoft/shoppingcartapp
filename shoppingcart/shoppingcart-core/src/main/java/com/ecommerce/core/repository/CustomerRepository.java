package com.ecommerce.core.repository;

import com.ecommerce.core.entities.Customer;
import com.ecommerce.core.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Siva
 *
 */

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	Customer findByEmail(String email);

	@Query("select o from Order o where o.customer.email=?1")
	List<Order> getCustomerOrders(String email);

}
