package lv.alija.bookShop.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lv.alija.bookShop.business.service.BookService;
import lv.alija.bookShop.exception.BookControllerException;
import lv.alija.bookShop.model.Book;
import lv.alija.bookShop.swagger.DescriptionVariables;
import lv.alija.bookShop.swagger.HTMLResponseMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;

@Api(tags = {DescriptionVariables.BOOK})
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @GetMapping("/list")
    @ApiOperation(value = "Find list of all books",
            notes = "Returns the entire list of books",
            response = Book.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200, response = Book.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public MappingJacksonValue findAllBooks() {
        log.info("Retrieve list of the books.");
        List<Book> books = bookService.findAllBooks();
        log.debug("Book list is found.Size: {}", books::size);
        return allPropertiesToGET(books);
    }

    @GetMapping("/filtered")
    @ApiOperation(value = "Find list of all books with title, author and price",
            notes = "Returns the entire list of books only with author, title and price",
            response = Book.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200, response = Book.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public MappingJacksonValue findAllBooksFiltered() {
        log.info("Retrieve list of the books only with book name and price.");
        List<Book> books = bookService.findAllBooks();
        log.debug("Book list is found.Size: {}", books::size);
        return filterSomeProperties(books);
    }

    @GetMapping("/list/{author}")
    @ApiOperation(value = "Find list of books by author",
            notes = "Returns the entire list of books by author",
            response = Book.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200, response = Book.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public MappingJacksonValue findAllByAuthor(@NonNull @PathVariable("author") String author) {
        log.info("Retrieve list of the books by author {}.", author);
        List<Book> books = bookService.findByAuthor(author);
        log.debug("Book list by author is found.Size: {}", books::size);
        return allPropertiesToGET(books);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find the book by id",
            notes = "Provide an id to search specific book in database",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public MappingJacksonValue findBookById(@ApiParam(value = "id of the book", required = true)
                                             @NonNull @PathVariable("id") @Min(value=1) Long id) {
        log.info("Retrieve book by book id {}.", id);
        Optional<Book> book = bookService.findBookById(id);
        log.debug("Book with id {} is found: {}", id, book);
        return allPropertiesToGET(book);
    }

    @PostMapping
    @ApiOperation(value = "Saves the book in the database",
            notes = "If provide valid book, saves it",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 409, message = HTMLResponseMessages.HTTP_409),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book, BindingResult bindingResult) throws Exception {
        log.info("Create new book by passing: {}", book);
         if (bindingResult.hasErrors()) {
            log.error("New book is not created: error {}", bindingResult);
             throw new BookControllerException(HttpStatus.BAD_REQUEST, "Bad request to create book");
        }
             Book bookSaved = bookService.saveBook(book);
             log.debug("New book is created: {}", book);
             return filterAllProperties(bookSaved);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the book by id",
            notes = "Updates the book if provided id exists",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = HTMLResponseMessages.HTTP_202),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 406, message = HTMLResponseMessages.HTTP_406),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Book> updateBookById(@ApiParam(value = "id of the book", required = true)
                                               @PathVariable @NotNull Long id,
                                              @Valid @RequestBody Book updatedBook,
                                              BindingResult bindingResult) throws Exception {
        log.info("Update existing book with Id: {} and new body: {}", id, updatedBook);
        if (bindingResult.hasErrors()) {
            log.error("Book is not updated: error {}", bindingResult);
            throw new BookControllerException(HttpStatus.BAD_REQUEST, "Bad request to update book");
        }
        Optional<Book> book = bookService.findBookById(id);
        bookService.updateBook(updatedBook, id);
        log.debug("Book with id {} is updated: {}", id, updatedBook);
        return filterAllProperties(updatedBook);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes the book by id",
            notes = "Deletes the book if provided id exists",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITHOUT_DATA),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBookById(@ApiParam(value = "The id of the book", required = true)
                                               @NonNull @PathVariable Long id) {
        log.info("Delete book by passing Id, where id is: {}", id);
        Optional<Book> book = bookService.findBookById(id);
        bookService.deleteBookById(id);
        log.debug("Book with id {} is deleted: {}", id, book);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     *      Method to use all Book model properties
     * @param book Object is Book model
     * @return object with all the parameters, that is declared in Book model
     */
    public ResponseEntity<Book> filterAllProperties(Book book ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAll();
        FilterProvider filters = new SimpleFilterProvider().addFilter("BooksFilter", filter);
        ObjectWriter writer = mapper.writer(filters);
        String writeValueAsString = writer.writeValueAsString(book);
        book = mapper.readValue(writeValueAsString, Book.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    /**
     * Method to use only needed Book model properties
     * @param obj In such case object is Book model
     * @return object with only three parameters: title. author and price
     */
    public MappingJacksonValue filterSomeProperties(Object obj ){
        SimpleBeanPropertyFilter  filter = SimpleBeanPropertyFilter.filterOutAllExcept("title", "author", "price");
        FilterProvider filters = new SimpleFilterProvider().addFilter("BooksFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(obj);
        mapping.setFilters(filters);
        return mapping;
    }
    /**
     * Method to use only needed Book model properties
     * @param obj In such case object is Book model
     * @return object with only three parameters: title. author and price
     */
    public MappingJacksonValue allPropertiesToGET(Object obj ){
        SimpleBeanPropertyFilter  filter = SimpleBeanPropertyFilter.serializeAll();
        FilterProvider filters = new SimpleFilterProvider().addFilter("BooksFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(obj);
        mapping.setFilters(filters);
        return mapping;
    }
}

