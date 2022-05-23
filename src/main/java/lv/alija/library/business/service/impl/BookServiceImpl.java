package lv.alija.library.business.service.impl;


import lombok.extern.log4j.Log4j2;
import lv.alija.library.business.mapper.BookMapper;
import lv.alija.library.business.repository.BookRepository;
import lv.alija.library.business.repository.model.BookDAO;
import lv.alija.library.business.service.BookService;
import lv.alija.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookMapper bookMapper;

    @Override
    public List<Book> findAllBooks() {
        List<BookDAO> booksDAO = bookRepository.findAll();

        return booksDAO.stream().map(bookMapper::bookDAOToBook)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        Optional<Book> bookById = bookRepository.findById(id)
                .flatMap(bookDAO -> Optional.ofNullable(bookMapper.bookDAOToBook(bookDAO)));

        return bookById;
    }

    @Override
    public Book saveBook(Book book) throws Exception {
        BookDAO bookDAO = bookMapper.bookToBookDAO(book);
        BookDAO bookSaved = bookRepository.save(bookDAO);

        return bookMapper.bookDAOToBook(bookSaved);
    }
}
