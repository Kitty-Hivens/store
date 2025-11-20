package store.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "clients")
@Data // Генерирует геттеры, сеттеры, toString, equals, hashCode
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    // Один клиент может иметь много заказов
    // 'mappedBy' говорит, что связь управляется полем 'client' в классе 'Order'
    @JsonManagedReference
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    public Client(String name, String email) {
        this.name = name;
        this.email = email;
    }
}