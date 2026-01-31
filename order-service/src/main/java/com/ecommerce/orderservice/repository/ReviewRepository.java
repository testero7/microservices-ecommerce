package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductId(Integer productId);
    List<Review> findByUserId(Integer userId);
}