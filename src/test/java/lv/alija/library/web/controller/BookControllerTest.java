package lv.alija.library.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.alija.library.business.service.impl.BookServiceImpl;
import lv.alija.library.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    public static String URL = "/book";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookController bookController;

    @MockBean
    private BookServiceImpl bookService;

    @Test
    void findAllBooksTest() throws Exception {
        List<Book> bookList = createBookList();
        when(bookService.findAllBooks()).thenReturn(bookList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/list"))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Title1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("Author1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value("Genre1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseYear").value(2022L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value("000-00-00-0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(2L))
                .andExpect(status().isOk());
        verify(bookService, times(1)).findAllBooks();
    }

    @Test
    void findAllBooks_InvalidTest(){}

    @Test
    void findBookByIdTest() {
    }

    @Test
    void findBookById_InvalidTest() {}

    @Test
    void saveBookTest() {
    }

    @Test
    void saveBookTest_InvalidTest() {}

    @Test
    void saveBookTest_InvalidDuplicateTest() { }

    @Test
    void updateBookByIdTest() {
    }

    @Test
    void updateBookTest_InvalidTest() {

    }

    @Test
    void deleteBookByIdTest() {
    }
    @Test
    void deleteBookById_InvalidTest() {}

    private List<Book> createBookList(){
        List<Book> list = new ArrayList<>();
        Book book1 = createBook();
        Book book2 = createBook();
        list.add(book1);
        list.add(book2);
       return list;
    }
    private Book createBook(){
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title1");
        book.setAuthor("Author1");
        book.setGenre("Genre1");
        book.setReleaseYear(2022L);
        book.setIsbn("000-00-00-0001");
        book.setQuantity(2L);
        return book;
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}