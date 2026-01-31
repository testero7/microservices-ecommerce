package com.ecommerce.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Integer productId;
    private String imagePath;

    @NotBlank(message = "Nazwa produktu nie może być pusta.")
    @Pattern(regexp = "^[A-Z].*", message = "Nazwa produktu powinna zaczynać się od dużej litery.")
    private String productName;

    private Double price;
    private String specification;
    private Integer quantity;
    private String categoryName;
}