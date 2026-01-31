package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.AddressDTO;
import com.ecommerce.orderservice.model.Address;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderItem;
import com.ecommerce.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cart.service.url}")
    private String cartServiceUrl;

    @Value("${product.service.url}")
    private String productServiceUrl;

    public Order createOrder(Integer userId, AddressDTO addressDTO) {
        // Pobierz zawartość koszyka
        String cartUrl = cartServiceUrl + "/api/cart/" + userId;
        List<Map<String, Object>> cartItems;
        try {
            cartItems = restTemplate.getForObject(cartUrl, List.class);
        } catch (Exception e) {
            throw new RuntimeException("Najpierw dodaj produkt do koszyka!");
        }

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Koszyk jest pusty!");
        }

        // Utwórz zamówienie
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus("ZATWIERDZONE");

        // Dodaj adres
        Address address = new Address();
        address.setBuildingNo(addressDTO.getBuildingNo());
        address.setCity(addressDTO.getCity());
        address.setStreet(addressDTO.getStreet());
        address.setCountry(addressDTO.getCountry());
        address.setPincode(addressDTO.getPincode());
        order.setAddress(address);

        // Przenieś produkty z koszyka do zamówienia
        List<OrderItem> orderItems = new ArrayList<>();
        for (Map<String, Object> item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId((Integer) item.get("productId"));
            orderItem.setProductName((String) item.get("productName"));
            orderItem.setImagePath((String) item.get("imagePath"));
            orderItem.setPrice((Double) item.get("price"));
            orderItem.setQuantity((Integer) item.get("quantity"));
            orderItems.add(orderItem);

            // Zaktualizuj stan magazynowy
            String updateStockUrl = productServiceUrl + "/api/products/" +
                    orderItem.getProductId() +
                    "/update-stock?quantity=" + orderItem.getQuantity();
            restTemplate.postForObject(updateStockUrl, null, String.class);
        }

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        // Wyczyść koszyk
        String clearCartUrl = cartServiceUrl + "/api/cart/clear/" + userId;
        restTemplate.delete(clearCartUrl);

        return order;
    }

    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia o id: " + orderId));
    }

    public List<Order> getAllOrdersByUser(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            throw new RuntimeException("Nie znaleziono żadnych zamówień!");
        }
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new RuntimeException("Nie znaleziono żadnych zamówień!");
        }
        return orders;
    }

    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = getOrderById(orderId);
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    public String cancelOrder(Integer orderId) {
        Order order = getOrderById(orderId);

        if ("ANULOWANE".equals(order.getOrderStatus())) {
            return "Zamówienie zostało już anulowane!";
        }

        order.setOrderDate(LocalDate.now());
        order.setOrderStatus("ANULOWANE");

        // Przywróć stan magazynowy
        for (OrderItem item : order.getOrderItems()) {
            String restoreStockUrl = productServiceUrl + "/api/products/" +
                    item.getProductId() +
                    "/restore-stock?quantity=" + item.getQuantity();
            restTemplate.postForObject(restoreStockUrl, null, String.class);
        }

        orderRepository.save(order);
        return "Udało się anulować zamówienie!";
    }
}