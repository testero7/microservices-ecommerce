package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.model.Review;
import com.ecommerce.orderservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> getReviewsByProduct(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> getReviewsByUser(Integer userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}