/**
 * 
 */
package com.ecommerce.site.web.controllers;

import java.util.List;

import javax.validation.Valid;

import com.ecommerce.core.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.core.entities.Customer;
import com.ecommerce.core.entities.Order;
import com.ecommerce.site.web.validators.CustomerValidator;

/**
 * @author Siva
 *
 */
@Controller
@Slf4j
public class CustomerController extends SCartSiteBaseController
{	
	@Autowired private CustomerService customerService;
	@Autowired private CustomerValidator customerValidator;
	@Autowired protected PasswordEncoder passwordEncoder;
	
	@Override
	public String getHeaderTitle()
	{
		return "Login/Register";
	}

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerForm(Model model)
	{
		model.addAttribute("customer", new Customer());
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String register(@Valid @ModelAttribute("customer") Customer customer, BindingResult result,
			Model model, RedirectAttributes redirectAttributes)
	{
		customerValidator.validate(customer, result);
		if(result.hasErrors()){
			return "register";
		}
		String password = customer.getPassword();
		String encodedPwd = passwordEncoder.encode(password);
		customer.setPassword(encodedPwd);
		
		Customer persistedCustomer = customerService.createCustomer(customer);
		log.debug("Created new Customer with id : {} and email : {}", persistedCustomer.getId(), persistedCustomer.getEmail());
		redirectAttributes.addFlashAttribute("info", "Customer created successfully");
		return "redirect:/login";
	}
	
	
	@RequestMapping(value="/myAccount", method=RequestMethod.GET)
	public String myAccount(Model model)
	{
		String email = getCurrentUser().getCustomer().getEmail();
		Customer customer = customerService.getCustomerByEmail(email);
		model.addAttribute("customer", customer);
		List<Order> orders = customerService.getCustomerOrders(email);
		model.addAttribute("orders", orders);
		return "myAccount";
	}
}
