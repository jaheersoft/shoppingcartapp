/**
 * 
 */
package com.ecommerce.site.security;

import com.ecommerce.core.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.core.entities.Customer;



/**
 * @author Siva
 *
 */
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService
{
	private CustomerService customerService;

	@Autowired
	public CustomUserDetailsService(CustomerService customerService) {
		this.customerService = customerService;
	}

	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {
		Customer customer = customerService.getCustomerByEmail(email);
		if(customer == null){
			throw new UsernameNotFoundException("Email "+email+" not found");
		}
		return new AuthenticatedUser(customer);
	}

}
