package lv.alija.bookShop.business.repository;

import lv.alija.bookShop.business.repository.model.ClientDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientDAO, Long> {
}
