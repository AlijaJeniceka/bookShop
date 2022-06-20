package lv.alija.bookShop.business.repository;

import lv.alija.bookShop.business.repository.model.BookDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookDAO, Long> {

   List<BookDAO> findByAuthor(String author);
}
