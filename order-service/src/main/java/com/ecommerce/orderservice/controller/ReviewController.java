package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.ReviewDTO;
import com.ecommerce.orderservice.model.Review;
import com.ecommerce.orderservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<String> addReview(@RequestBody ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setUserId(reviewDTO.getUserId());
        review.setProductId(reviewDTO.getProductId());
        review.setDescription(reviewDTO.getDescription());
        reviewService.addReview(review);
        return ResponseEntity.ok("Udało się dodać recenzję.");
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable Integer productId) {
        List<Review> reviews = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable Integer userId) {
        List<Review> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }
}