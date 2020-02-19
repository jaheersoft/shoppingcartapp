package com.ecommerce.core.repository;

import com.ecommerce.core.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Siva
 *
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category getByName(String name);
}
