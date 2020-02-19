package com.ecommerce.core.repository;

import com.ecommerce.core.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Siva
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	Product findBySku(String sku);

	@Query("select p from Product p where p.name like ?1 or p.sku like ?1 or p.description like ?1")
	List<Product> search(String query);

}
