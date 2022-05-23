package lv.alija.library.business.repository;

import lv.alija.library.business.repository.model.BookDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookDAO, Long> {
}
