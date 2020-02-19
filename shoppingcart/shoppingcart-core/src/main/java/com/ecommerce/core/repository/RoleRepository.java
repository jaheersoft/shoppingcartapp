package com.ecommerce.core.repository;

import com.ecommerce.core.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Siva
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>
{

	Role findByName(String name);

}
