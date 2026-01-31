package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductDTO;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Product product = productService.createProduct(productDTO);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer productId,
                                           @Valid @RequestBody ProductDTO productDTO,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Product product = productService.updateProduct(productDTO, productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // Endpoint dla komunikacji między serwisami
    @GetMapping("/{productId}/availability")
    public ResponseEntity<Map<String, Boolean>> checkAvailability(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        boolean available = productService.checkProductAvailability(productId, quantity);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/update-stock")
    public ResponseEntity<String> updateStock(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        productService.updateStock(productId, quantity);
        return ResponseEntity.ok("Stock updated");
    }

    @PostMapping("/{productId}/restore-stock")
    public ResponseEntity<String> restoreStock(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        productService.restoreStock(productId, quantity);
        return ResponseEntity.ok("Stock restored");
    }
}