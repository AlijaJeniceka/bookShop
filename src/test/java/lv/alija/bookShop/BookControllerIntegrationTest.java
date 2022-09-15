package lv.alija.bookShop;

import lv.alija.bookShop.model.Book;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void findAllBooksIntTest() throws JSONException {
        String response = this.restTemplate.getForObject("/book/list", String.class );
        JSONAssert.assertEquals("[{id:1}, {id:2}]", response, false);
    }

    @Test
    public void return400AllBooksNotFound_incorrectURL(){
        ResponseEntity<String> err = restTemplate.getForEntity("/book/li", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, err.getStatusCode());
    }

    @Test
    public void findBookByIdIntTest() throws JSONException {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/book/{id}", String.class, 1 );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void return400_FindById_NegativeOrNull_Url_ID(){
       ResponseEntity<String> err = restTemplate.getForEntity("/book/{id}", String.class, -1);
       assertEquals(HttpStatus.BAD_REQUEST, err.getStatusCode());
    }

    @Test
    public void return404_FindById_NotExisting_Url_Id(){
        ResponseEntity<String> err = restTemplate.getForEntity("/book/{id}", String.class, 63);
        assertEquals(HttpStatus.NOT_FOUND, err.getStatusCode());
    }

    @Test
    public void findBookByAuthorIntTest() throws JSONException {
        String response = this.restTemplate.getForObject("/book/list/{author}", String.class, "Author" );
        JSONAssert.assertEquals("[{id:1}]", response, false);
    }

    @Test
    public void return404_FindByAuthor_BadUrlAuthor(){
        ResponseEntity<String> err = restTemplate.getForEntity("/book/list/{author}", String.class, "Anything");
        assertEquals(HttpStatus.NOT_FOUND, err.getStatusCode());
    }

    @Test
    public void saveNewBookTest() throws JSONException {
        Book book = new Book();
        book.setId(3L);
        book.setTitle("Title New For Test");
        book.setAuthor("Author3");
        book.setGenre("Genre3");
        book.setReleaseYear(2022L);
        book.setIsbn("000-00-00-0006");
        book.setQuantity(1L);
        book.setPrice(15L);
        ResponseEntity<Book> responseEntity = this.restTemplate.postForEntity("/book", book, Book.class );
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(book, responseEntity.getBody()));
    }

    @Test
    public void saveNewBookTest_incorrectInputForNewBook() throws JSONException {
        Book book = new Book();
        book.setId(3L);
        book.setTitle("Title New For Test");
        book.setQuantity(1L);
        book.setPrice(15L);
        ResponseEntity<Book> responseEntity = this.restTemplate.postForEntity("/book", book, Book.class );
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void saveNewBookTest_409Conflict_sameISBN() throws JSONException {
        Book book = new Book();
        book.setId(3L);
        book.setTitle("Title New For Test");
        book.setAuthor("Author3");
        book.setGenre("Genre3");
        book.setReleaseYear(2022L);
        book.setIsbn("000-00-00000-00-0");
        book.setQuantity(1L);
        book.setPrice(15L);
        ResponseEntity<Book> responseEntity = this.restTemplate.postForEntity("/book", book, Book.class );
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void updateNewBookTest() throws JSONException {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title Update For Test");
        book.setAuthor("Author3");
        book.setGenre("Genre3");
        book.setReleaseYear(2022L);
        book.setIsbn("000-00-00-0006");
        book.setQuantity(1L);
        book.setPrice(15L);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Book> entity = new HttpEntity<Book>(book,headers);
        ResponseEntity<Book> responseEntity =this.restTemplate.exchange("/book/1", HttpMethod.PUT, entity, Book.class, book);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void updateNewBookTest_badRequest400_missingInfoInput() throws JSONException {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title Update For Test");
        book.setIsbn("000-00-00-0006");
        book.setQuantity(1L);
        book.setPrice(15L);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Book> entity = new HttpEntity<Book>(book,headers);
        ResponseEntity<Book> responseEntity =this.restTemplate.exchange("/book/1", HttpMethod.PUT, entity, Book.class, book);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void updateNewBookTest_badRequest400_IdNegativeOrNull() throws JSONException {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title Update For Test");
        book.setAuthor("Author3");
        book.setGenre("Genre3");
        book.setReleaseYear(2022L);
        book.setIsbn("000-00-00-0006");
        book.setQuantity(1L);
        book.setPrice(15L);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Book> entity = new HttpEntity<Book>(book,headers);
        ResponseEntity<Book> responseEntity =this.restTemplate.exchange("/book/-1", HttpMethod.PUT, entity, Book.class, book);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void updateNewBookTest_NotAcceptable_incorrectId() throws JSONException {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Title Update For Test");
        book.setAuthor("Author3");
        book.setGenre("Genre3");
        book.setReleaseYear(2022L);
        book.setIsbn("000-00-00-0006");
        book.setQuantity(1L);
        book.setPrice(15L);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Book> entity = new HttpEntity<Book>(book,headers);
        ResponseEntity<Book> responseEntity = this.restTemplate.exchange("/book/1", HttpMethod.PUT, entity, Book.class, book);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void updateNewBookTest_NotFound_incorrectId() throws JSONException {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title Update For Test");
        book.setAuthor("Author3");
        book.setGenre("Genre3");
        book.setReleaseYear(2022L);
        book.setIsbn("000-00-00-0006");
        book.setQuantity(1L);
        book.setPrice(15L);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Book> entity = new HttpEntity<Book>(book,headers);
        ResponseEntity<Book> responseEntity = this.restTemplate.exchange("/book/63", HttpMethod.PUT, entity, Book.class, book);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void deleteBookByIdIntTest() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("/book/2", HttpMethod.DELETE, entity, String.class);
       assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode() );
    }

    @Test
    public void return404AllBooks_notFound(){
        this.restTemplate.delete("/book/1");
        this.restTemplate.delete("/book/2");
        ResponseEntity<Void> emptyList = restTemplate.getForEntity("/book/list", Void.class);
        assertEquals(HttpStatus.NOT_FOUND, emptyList.getStatusCode());

    }

}