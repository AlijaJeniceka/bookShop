package lv.alija.bookShop.business.service;

import lv.alija.bookShop.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> findAllBooks();

    Optional<Book> findBookById(Long id);

    Book saveBook(Book book) throws Exception;

    void deleteBookById(Long id);

    Book updateBook(Book book) throws Exception;

    List<Book> findBookListByAuthor(String author);
}
