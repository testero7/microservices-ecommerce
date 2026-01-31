package com.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String buildingNo;
    private String city;
    private String street;
    private String country;
    private String pincode;
}