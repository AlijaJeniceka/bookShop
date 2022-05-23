package lv.alija.library.business.service;

import lv.alija.library.model.Book;

import java.util.List;
import java.util.Optional;


public interface BookService {

    List<Book> findAllBooks();

    Optional<Book> findBookById(Long id);

    Book saveBook(Book book) throws Exception;


}
