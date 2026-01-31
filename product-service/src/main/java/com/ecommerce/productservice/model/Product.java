package com.ecommerce.productservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;
    private String imagePath;
    private String productName;
    private Double price;
    private String specification;
    private Integer quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;
}