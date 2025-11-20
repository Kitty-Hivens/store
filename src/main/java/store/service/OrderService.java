package store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.model.Client;
import store.model.Order;
import store.model.OrderItem;
import store.model.Product;
import store.repository.ClientRepository;
import store.repository.OrderRepository;
import store.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Client addClient(String name, String email) {
        Client client = new Client(name, email);
        return clientRepository.save(client);
    }

    public Product addProduct(String name, Double price) {
        Product product = new Product(name, price);
        return productRepository.save(product);
    }

    @Transactional // Гарантирует, что все операции с базой либо пройдут, либо откатятся
    public Order createOrder(Long clientId, Map<Long, Integer> productsMap) {
        // Map<Long, Integer> - это map из <ID Товара, Количество>

        // Находим клиента
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));

        // Создаем новый заказ
        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        double total = 0.0;

        // Заполняем заказ товарами
        for (Map.Entry<Long, Integer> entry : productsMap.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            // Находим товар в базе
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Товар не найден: " + productId));

            // Создаем позицию в заказе
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPriceAtPurchase(product.getPrice()); // Фиксируем цену

            // Добавляем позицию в заказ
            order.addOrderItem(orderItem);

            // Считаем общую сумму
            total += product.getPrice() * quantity;
        }

        order.setTotalPrice(total);

        // Сохраняем заказ (благодаря CascadeType.ALL, OrderItems сохранятся вместе с ним)
        return orderRepository.save(order);
    }

    public Client getClientById(Long clientId) {
        // findById возвращает Optional<Client>,
        // .orElseThrow() выкинет ошибку, если клиент не найден.
        // Spring Boot красиво превратит эту ошибку в "404 Not Found"
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Клиент с ID " + clientId + " не найден"));
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар с ID " + productId + " не найден"));
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ с ID " + orderId + " не найден"));
    }
}