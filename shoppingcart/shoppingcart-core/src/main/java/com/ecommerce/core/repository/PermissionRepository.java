package com.ecommerce.core.repository;

import com.ecommerce.core.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Siva
 *
 */

public interface PermissionRepository extends JpaRepository<Permission, Integer>
{

}
