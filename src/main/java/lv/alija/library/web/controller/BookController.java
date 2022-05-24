package lv.alija.library.web.controller;


import lombok.extern.log4j.Log4j2;
import lv.alija.library.business.service.BookService;
import lv.alija.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/list")
    public ResponseEntity<List<Book>> findAllBooks() {
        log.info("Retrieve list of the books.");
        List<Book> books = bookService.findAllBooks();
        log.debug("Book list is found.Size: {}", books::size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findBookById(@NonNull @PathVariable("id") Long id) {
        log.info("Retrieve book by book id {}.", id);
        Optional<Book> book = bookService.findBookById(id);
        log.debug("Book with id {} is found: {}", id, book);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book, BindingResult bindingResult) throws Exception {
        log.info("Create new book by passing: {}", book);
        if (bindingResult.hasErrors()) {
            log.error("New book is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        Book bookSaved = bookService.saveBook(book);
        log.debug("New book is created: {}", book);
        return new ResponseEntity<>(bookSaved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Book> updateBookById(@PathVariable @NotNull Long id,
                                               @Valid @RequestBody Book updatedBook,
                                               BindingResult bindingResult) throws Exception {
        log.info("Update existing book with Id: {} and new body: {}", id, updatedBook);
        Optional<Book> book = bookService.findBookById(updatedBook.getId());
        if (bindingResult.hasErrors() || !id.equals(updatedBook.getId())) {
            log.warn("Book for update with id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
        bookService.updateBook(updatedBook);
        log.debug("Book with id {} is updated: {}", id, updatedBook);
        return new ResponseEntity<>(updatedBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        log.info("Delete book by passing Id, where id is: {}", id);
        if (id <= 0) {
            log.warn("Book id is not null or negative number. Insert only positive numbers. ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Book> book = bookService.findBookById(id);
        if (!book.isPresent()) {
            log.warn("Book for delete with id {} is not found. ", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.deleteBookById(id);
        log.debug("Book with id {} is deleted: {}", id, book);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
