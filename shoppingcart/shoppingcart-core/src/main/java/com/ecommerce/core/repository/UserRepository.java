package com.ecommerce.core.repository;

import com.ecommerce.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Siva
 *
 */

public interface UserRepository extends JpaRepository<User, Integer>
{

	User findByEmail(String email);

}
