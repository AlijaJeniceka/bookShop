package lv.alija.library.web.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import lv.alija.library.business.service.BookService;
import lv.alija.library.model.Book;
import lv.alija.library.swagger.DescriptionVariables;
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

@Api(tags = {DescriptionVariables.BOOK})
@Log4j2
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/list")
    @ApiOperation(value = "Find list of all books",
            notes = "Returns the entire list of books",
            response = Book.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded", response = Book.class, responseContainer = "List"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<List<Book>> findAllBooks() {
        log.info("Retrieve list of the books.");
        List<Book> books = bookService.findAllBooks();
        log.debug("Book list is found.Size: {}", books::size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find the book by id",
            notes = "Provide an id to search specific book in database",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<Book> findBookById(@ApiParam(value = "id of the book", required = true)
                                             @NonNull @PathVariable("id") Long id) {
        log.info("Retrieve book by book id {}.", id);
        Optional<Book> book = bookService.findBookById(id);
        log.debug("Book with id {} is found: {}", id, book);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Saves the book in the database",
            notes = "If provide valid book, saves it",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The book is successfully saved"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
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
    @ApiOperation(value = "Updates the book by id",
            notes = "Updates the book if provided id exists",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The book is successfully updated"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Book> updateBookById(@ApiParam(value = "id of the book", required = true)
                                               @PathVariable @NotNull Long id,
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
    @ApiOperation(value = "Deletes the book by id",
            notes = "Deletes the book if provided id exists",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The book is successfully deleted"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBookById(@ApiParam(value = "The id of the book", required = true)
                                               @NonNull @PathVariable Long id) {
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
