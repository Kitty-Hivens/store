package store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.dto.CreationRequest;
import store.dto.OrderRequest;
import store.model.Client;
import store.model.Order;
import store.model.Product;
import store.service.OrderService;

@RestController
@RequestMapping("/api") // Все URL будут начинаться с /api
public class ApiController {

    @Autowired
    private OrderService orderService;

    // 1. Конечная точка для добавления клиента
    @PostMapping("/clients")
    public ResponseEntity<Client> addClient(@RequestBody CreationRequest request) {
        Client client = orderService.addClient(request.getName(), request.getEmail());
        return ResponseEntity.ok(client);
    }

    // 2. Конечная точка для добавления товара
    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody CreationRequest request) {
        Product product = orderService.addProduct(request.getName(), request.getPrice());
        return ResponseEntity.ok(product);
    }

    // 3. Конечная точка для оформления заказа
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        try {
            Order order = orderService.createOrder(request.getClientId(), request.getProducts());
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            // Возвращаем ошибку, если клиент или товар не найдены
            return ResponseEntity.badRequest().body(null); 
        }
    }

    /**
     * Получить клиента по ID
     * @PathVariable("id") говорит Spring: "возьми {id} из URL и передай в метод"
     */
    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClient(@PathVariable("id") Long id) {
        try {
            Client client = orderService.getClientById(id);
            return ResponseEntity.ok(client);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Возвращаем 404
        }
    }

    /**
     * Получить товар по ID
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        try {
            Product product = orderService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Получить заказ по ID
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Long id) {
        try {
            Order order = orderService.getOrderById(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}