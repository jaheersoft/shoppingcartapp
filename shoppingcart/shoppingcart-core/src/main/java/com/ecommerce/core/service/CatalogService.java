package com.ecommerce.core.service;

import com.ecommerce.core.exception.JCartException;
import com.ecommerce.core.repository.CategoryRepository;
import com.ecommerce.core.repository.ProductRepository;
import com.ecommerce.core.entities.Category;
import com.ecommerce.core.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Siva
 *
 */
@Service
@Transactional
public class CatalogService {
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @Autowired
	public CatalogService(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	public List<Category> getAllCategories() {
		
		return categoryRepository.findAll();
	}
	
	public List<Product> getAllProducts() {
		
		return productRepository.findAll();
	}

	public Category getCategoryByName(String name) {
		return categoryRepository.getByName(name);
	}
	
	public Category getCategoryById(Integer id) {
		return categoryRepository.getOne(id);
	}

	public Category createCategory(Category category) {
		Category persistedCategory = getCategoryByName(category.getName());
		if(persistedCategory != null){
			throw new JCartException("Category with name "+category.getName()+" already exist");
		}
		return categoryRepository.save(category);
	}

	public Category updateCategory(Category category) {
		Category persistedCategory = getCategoryById(category.getId());
		if(persistedCategory == null){
			throw new JCartException("Category with Id"+category.getId()+" doesn't exist");
		}
		persistedCategory.setDescription(category.getDescription());
		persistedCategory.setDisplayOrder(category.getDisplayOrder());
		persistedCategory.setDisabled(category.isDisabled());
		return categoryRepository.save(persistedCategory);
	}

	public Product getProductById(Integer id) {
		return productRepository.getOne(id);
	}
	
	public Product getProductBySku(String sku) {
		return productRepository.findBySku(sku);
	}
	
	public Product createProduct(Product product) {
		Product persistedProduct = getProductBySku(product.getName());
		if(persistedProduct != null){
			throw new JCartException("Product SKU "+product.getSku()+" already exist");
		}
		return productRepository.save(product);
	}
	
	public Product updateProduct(Product product) {
		Product persistedProduct = getProductById(product.getId());
		if(persistedProduct == null){
			throw new JCartException("Product "+product.getId()+" doesn't exist");
		}
		persistedProduct.setDescription(product.getDescription());
		persistedProduct.setDisabled(product.isDisabled());
		persistedProduct.setPrice(product.getPrice());
		persistedProduct.setCategory(getCategoryById(product.getCategory().getId()));
		return productRepository.save(persistedProduct);
	}

	public List<Product> searchProducts(String query) {
		return productRepository.search("%"+query+"%");
	}
}
