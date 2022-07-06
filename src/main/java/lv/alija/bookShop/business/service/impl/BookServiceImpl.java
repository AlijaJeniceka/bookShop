package lv.alija.bookShop.business.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lv.alija.bookShop.business.mapper.BookMapper;
import lv.alija.bookShop.business.repository.BookRepository;
import lv.alija.bookShop.business.repository.model.BookDAO;
import lv.alija.bookShop.business.service.BookService;
import lv.alija.bookShop.exception.BookControllerException;
import lv.alija.bookShop.model.Book;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Cacheable(value = "bookList")
    @Scheduled(fixedDelay = 300000)
    @Override
    public List<Book> findAllBooks() {
        List<BookDAO> booksDAO = bookRepository.findAll();
        log.info("Get book list. Size is : {}", booksDAO::size);
        if (booksDAO.isEmpty()) {
            log.warn("Books list is not found. ");
            throw new BookControllerException(HttpStatus.NOT_FOUND, "Book list is empty");
        }
        return booksDAO.stream().map(bookMapper::bookDAOToBook)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthor(String author){
        List<BookDAO> bookDAOList = bookRepository.findByAuthor(author);
        if (bookDAOList.isEmpty()) {
            log.warn("Books list by author is not found. ");
            throw new BookControllerException(HttpStatus.NOT_FOUND, "Book list by this author is empty");
        }
        return bookDAOList.stream().map(bookMapper::bookDAOToBook)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        if (id <= 0) {
            log.warn("Book id is not null or negative number. Insert only positive numbers. ");
            throw new BookControllerException(HttpStatus.BAD_REQUEST, "Id should be bigger then null");
        }
        Optional<Book> bookById = bookRepository.findById(id)
                .flatMap(bookDAO -> Optional.ofNullable(bookMapper.bookDAOToBook(bookDAO)));
        if (!bookById.isPresent()) {
            log.warn("Book with id {} is not found. ", id);
            throw new BookControllerException(HttpStatus.NOT_FOUND, "Book with this id is not found");
        }
        log.info("Book with id {} is {}", id, bookById);
        return bookById;
    }

    @CacheEvict(cacheNames = "bookList", allEntries = true)
    @Override
    public Book saveBook(Book book) throws Exception {
        if (!hasNoMatch(book)) {
            log.error("Book with same isbn number already exists. Conflict exception. ");
            throw new BookControllerException(HttpStatus.CONFLICT, "Book with same isbn number already exists");
        }
        BookDAO bookDAO = bookMapper.bookToBookDAO(book);
        BookDAO bookSaved = bookRepository.save(bookDAO);
        log.info("New book saved: {}", () -> bookSaved);
        return bookMapper.bookDAOToBook(bookSaved);
    }

    @CacheEvict(cacheNames = "bookList", allEntries = true)
    @Override
    public Book updateBook(Book book, Long id) throws Exception {
        if (!book.getId().equals(id)) {
            log.error("Book with this id is not possible to update ");
            throw new BookControllerException(HttpStatus.NOT_ACCEPTABLE, "Book is not possible to update");
        }
        BookDAO bookDAO = bookMapper.bookToBookDAO(book);
        BookDAO bookSaved = bookRepository.save(bookDAO);
        log.info("Book is updated: {}", () -> bookSaved);
        return bookMapper.bookDAOToBook(bookSaved);
    }

    @CacheEvict(cacheNames = "bookList", allEntries = true)
    @Override
    public void deleteBookById(Long id) {
        if (id <= 0) {
            log.warn("Book id is not null or negative number. Insert only positive numbers. ");
            throw new BookControllerException(HttpStatus.BAD_REQUEST, "Id should be bigger then null");
        }
        Optional<BookDAO> bookById = bookRepository.findById(id);
        if (!bookById.isPresent()) {
            log.warn("Book with id {} is not found. ", id);
            throw new BookControllerException(HttpStatus.NOT_FOUND, "Book with this id is not found");
        }
        bookRepository.deleteById(bookById.get().getId());
        log.info("Book with id {} is deleted", id);
    }

    public boolean hasNoMatch(Book book) {
        return bookRepository.findAll().stream()
                .noneMatch(newBook ->
                        newBook.getIsbn().equalsIgnoreCase(book.getIsbn())
                );
    }
}
