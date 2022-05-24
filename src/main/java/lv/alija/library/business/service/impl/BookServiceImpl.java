package lv.alija.library.business.service.impl;


import lombok.extern.log4j.Log4j2;
import lv.alija.library.business.mapper.BookMapper;
import lv.alija.library.business.repository.BookRepository;
import lv.alija.library.business.repository.model.BookDAO;
import lv.alija.library.business.service.BookService;
import lv.alija.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookMapper bookMapper;

    @Override
    public List<Book> findAllBooks() {
        List<BookDAO> booksDAO = bookRepository.findAll();
        log.info("Get book list. Size is : {}", booksDAO::size);
        return booksDAO.stream().map(bookMapper::bookDAOToBook)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        Optional<Book> bookById = bookRepository.findById(id)
                .flatMap(bookDAO -> Optional.ofNullable(bookMapper.bookDAOToBook(bookDAO)));
        log.info("Book with id {} is {}", id, bookById);
        return bookById;
    }

    @Override
    public Book saveBook(Book book) throws Exception {
        if (!hasNoMatch(book)) {
            log.error("Book with same isbn number already exists. Conflict exception. ");
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        BookDAO bookDAO = bookMapper.bookToBookDAO(book);
        BookDAO bookSaved = bookRepository.save(bookDAO);
        log.info("New book saved: {}", () -> bookSaved);
        return bookMapper.bookDAOToBook(bookSaved);
    }

    @Override
    public Book updateBook(Book book) throws Exception {
        BookDAO bookDAO = bookMapper.bookToBookDAO(book);
        BookDAO bookSaved = bookRepository.save(bookDAO);
        log.info("Book is updated: {}", () -> bookSaved);
        return bookMapper.bookDAOToBook(bookSaved);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
        log.info("Book with id {} is deleted", id);
    }

    public boolean hasNoMatch(Book book) {
        return bookRepository.findAll().stream()
                .noneMatch(newBook ->
                        newBook.getIsbn().equalsIgnoreCase(book.getIsbn())
                );
    }
}
