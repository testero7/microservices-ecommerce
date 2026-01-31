package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.model.Cart;
import com.ecommerce.cartservice.model.CartItem;
import com.ecommerce.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product.service.url}")
    private String productServiceUrl;

    public Cart getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }

    public String addProductToCart(Integer userId, Integer productId, Integer quantity) {
        // Pobierz informacje o produkcie z Product Service
        String url = productServiceUrl + "/api/products/" + productId;
        Map<String, Object> product;
        try {
            product = restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Nie znaleziono produktu");
        }

        // Sprawdź dostępność
        String availabilityUrl = productServiceUrl + "/api/products/" + productId +
                "/availability?quantity=" + quantity;
        Map<String, Boolean> availability = restTemplate.getForObject(availabilityUrl, Map.class);

        if (!availability.get("available")) {
            throw new RuntimeException("Nie ma już tego produktu");
        }

        Cart cart = getCartByUserId(userId);

        // Sprawdź czy produkt już jest w koszyku
        for (CartItem item : cart.getCartItems()) {
            if (item.getProductId().equals(productId)) {
                return "Produkt już został dodany do koszyka!";
            }
        }

        // Dodaj nowy produkt
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setProductName((String) product.get("productName"));
        cartItem.setImagePath((String) product.get("imagePath"));
        cartItem.setPrice((Double) product.get("price"));
        cartItem.setQuantity(quantity);

        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);

        return "Dodano produkt do koszyka";
    }

    public List<CartItem> getAllCartItems(Integer userId) {
        Cart cart = getCartByUserId(userId);
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Koszyk jest pusty!");
        }
        return cart.getCartItems();
    }

    public String removeProductFromCart(Integer userId, Integer productId) {
        Cart cart = getCartByUserId(userId);

        boolean removed = cart.getCartItems().removeIf(item ->
                item.getProductId().equals(productId)
        );

        if (!removed) {
            throw new RuntimeException("Produkt nie został dodany do koszyka!");
        }

        cartRepository.save(cart);
        return "Produkt został usunięty z koszyka!";
    }

    public CartItem updateQuantity(Integer userId, Integer productId, Integer quantity) {
        Cart cart = getCartByUserId(userId);

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Produkt nie jest w koszyku"));

        // Sprawdź dostępność
        String availabilityUrl = productServiceUrl + "/api/products/" + productId +
                "/availability?quantity=" + quantity;
        Map<String, Boolean> availability = restTemplate.getForObject(availabilityUrl, Map.class);

        if (!availability.get("available")) {
            throw new RuntimeException("Nie ma już tego produktu!");
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartItem;
    }

    public Cart clearCart(Integer userId) {
        Cart cart = getCartByUserId(userId);
        cart.getCartItems().clear();
        return cartRepository.save(cart);
    }
}