package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.model.Cart;
import com.ecommerce.cartservice.model.CartItem;
import com.ecommerce.cartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        String message = cartService.addProductToCart(userId, productId, quantity);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable Integer userId) {
        List<CartItem> items = cartService.getAllCartItems(userId);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        String message = cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/update-quantity")
    public ResponseEntity<CartItem> updateQuantity(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        CartItem item = cartService.updateQuantity(userId, productId, quantity);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Cart> clearCart(@PathVariable Integer userId) {
        Cart cart = cartService.clearCart(userId);
        return ResponseEntity.ok(cart);
    }
}