package com.ecommerce.core.repository;

import com.ecommerce.core.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Siva
 *
 */

public interface OrderRepository extends JpaRepository<Order, Integer>
{
	Order findByOrderNumber(String orderNumber);
}
