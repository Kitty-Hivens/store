package store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {}