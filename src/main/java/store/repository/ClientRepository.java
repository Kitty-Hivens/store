package store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {}