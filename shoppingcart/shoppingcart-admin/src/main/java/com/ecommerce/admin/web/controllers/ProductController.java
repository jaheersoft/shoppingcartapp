package com.ecommerce.admin.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.ecommerce.admin.web.security.SecurityUtil;
import com.ecommerce.core.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.core.exception.JCartException;
import com.ecommerce.admin.web.models.ProductForm;
import com.ecommerce.admin.web.utils.WebUtils;
import com.ecommerce.admin.web.validators.ProductFormValidator;
import com.ecommerce.core.entities.Category;
import com.ecommerce.core.entities.Product;

/**
 * @author Siva
 *
 */
@Controller
@Secured(SecurityUtil.MANAGE_PRODUCTS)
@Slf4j
public class ProductController extends SCartAdminBaseController
{
	private static final String viewPrefix = "products/";

	@Autowired
	private CatalogService catalogService;

	@Autowired private ProductFormValidator productFormValidator;

	@Override
	protected String getHeaderTitle()
	{
		return "Manage Products";
	}

	@ModelAttribute("categoriesList")
	public List<Category> categoriesList()
	{
		return catalogService.getAllCategories();
	}

	@RequestMapping(value="/products", method=RequestMethod.GET)
	public String listProducts(Model model) {
		model.addAttribute("products",catalogService.getAllProducts());
		return viewPrefix+"products";
	}

	@RequestMapping(value="/products/new", method=RequestMethod.GET)
	public String createProductForm(Model model) {
		ProductForm product = new ProductForm();
		model.addAttribute("product",product);
		//model.addAttribute("categoriesList",catalogService.getAllCategories());
		return viewPrefix+"create_product";
	}

	@RequestMapping(value="/products", method=RequestMethod.POST)
	public String createProduct(@Valid @ModelAttribute("product") ProductForm productForm, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		productFormValidator.validate(productForm, result);
		if(result.hasErrors()){
			return viewPrefix+"create_product";
		}
		Product product = productForm.toProduct();
		Product persistedProduct = catalogService.createProduct(product);
		productForm.setId(product.getId());
		this.saveProductImageToDisk(productForm);
		log.debug("Created new product with id : {} and name : {}", persistedProduct.getId(), persistedProduct.getName());
		redirectAttributes.addFlashAttribute("info", "Product created successfully");
		return "redirect:/products";
	}

	@RequestMapping(value="/products/{id}", method=RequestMethod.GET)
	public String editProductForm(@PathVariable Integer id, Model model) {
		Product product = catalogService.getProductById(id);
		model.addAttribute("product",ProductForm.fromProduct(product));
		//model.addAttribute("categoriesList",catalogService.getAllCategories());
		return viewPrefix+"edit_product";
	}

	@RequestMapping(value="/products/images/{productImage}", method=RequestMethod.GET)
	public void showProductImage(@PathVariable String productImage, HttpServletRequest request, HttpServletResponse response) {
		try {
			FileSystemResource file = new FileSystemResource(WebUtils.getImagesDirectory() +productImage);
			response.setContentType("image/jpg");
			org.apache.commons.io.IOUtils.copy(file.getInputStream(), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value="/products/{productId}", method=RequestMethod.POST)
	public String updateProduct(@PathVariable Integer productId,
			@Valid @ModelAttribute("product") ProductForm productForm,
								BindingResult result,
			RedirectAttributes redirectAttributes) {
		productFormValidator.validate(productForm, result);
		if(result.hasErrors()){
			return viewPrefix+"edit_product";
		}
		productForm.setId(productId);
		Product product = productForm.toProduct();
		Product persistedProduct = catalogService.updateProduct(product);
		this.saveProductImageToDisk(productForm);
		log.debug("Updated product with id : {} and name : {}", persistedProduct.getId(), persistedProduct.getName());
		redirectAttributes.addFlashAttribute("info", "Product updated successfully");
		return "redirect:/products";
	}

	private void saveProductImageToDisk(ProductForm productForm) {
		MultipartFile file = productForm.getImage();
		if (file!= null && !file.isEmpty()) {
			String name = WebUtils.getImagesDirectory() + productForm.getId() + ".jpg";
			try {
				byte[] bytes = file.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				throw new JCartException(e);
			}
		}
	}
}
