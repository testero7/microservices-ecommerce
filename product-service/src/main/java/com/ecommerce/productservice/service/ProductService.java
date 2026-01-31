package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductDTO;
import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setImagePath(productDTO.getImagePath());
        product.setQuantity(productDTO.getQuantity());
        product.setSpecification(productDTO.getSpecification());
        product.setPrice(productDTO.getPrice());

        Category category = categoryRepository.findByCategoryName(productDTO.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(productDTO.getCategoryName());
                    return categoryRepository.save(newCategory);
                });

        product.setCategory(category);
        return productRepository.save(product);
    }

    public Product updateProduct(ProductDTO productDTO, Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductName(productDTO.getProductName());
        product.setQuantity(productDTO.getQuantity());
        product.setImagePath(productDTO.getImagePath());
        product.setSpecification(productDTO.getSpecification());
        product.setPrice(productDTO.getPrice());

        if (productDTO.getCategoryName() != null) {
            Category category = categoryRepository.findByCategoryName(productDTO.getCategoryName())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setCategoryName(productDTO.getCategoryName());
                        return categoryRepository.save(newCategory);
                    });
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Integer productId) {
        productRepository.deleteById(productId);
    }

    public boolean checkProductAvailability(Integer productId, Integer quantity) {
        Product product = getProductById(productId);
        return product.getQuantity() >= quantity;
    }

    public void updateStock(Integer productId, Integer quantity) {
        Product product = getProductById(productId);
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    public void restoreStock(Integer productId, Integer quantity) {
        Product product = getProductById(productId);
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }
}