package lv.alija.bookShop.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.alija.bookShop.business.service.impl.BookServiceImpl;
import lv.alija.bookShop.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(4L))
                .andExpect(status().isOk());
        verify(bookService, times(1)).findAllBooks();
    }


    @Test
    void findBookByIdTest() throws Exception {
        Optional<Book> book = Optional.of(createBook());
        when(bookService.findBookById(1L)).thenReturn(book);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Author1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Genre1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(2022L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("000-00-00-0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(4L))
                .andExpect(status().isOk());
        verify(bookService, times(1)).findBookById(1L);
    }

    @Test
    void findBookById_InvalidTest() throws Exception {
        Optional<Book> book = Optional.of(createBook());

        when(bookService.findBookById(3L)).thenReturn(Optional.empty());
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/3")
                        .content(asJsonString(book))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(bookService, times(0)).findBookById(null);
    }

    @Test
    void saveBookTest() throws Exception {
        Book book = createBook();

        when(bookService.saveBook(book)).thenReturn(book);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(book))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(bookService, times(1)).saveBook(book);
    }

    @Test
    void saveBookTest_InvalidTest() throws Exception {
        Book book = createBook();
        book.setTitle("");
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(book))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(bookService, times(0)).saveBook(book);
    }

    @Test
    void updateBookByIdTest() throws Exception {
        Book book = createBook();
        when(bookService.findBookById(book.getId())).thenReturn(Optional.of(book));
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(book))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(status().isCreated());
        verify(bookService, times(1)).updateBook(book);
    }

    @Test
    void updateBookByIdTest_InvalidTest() throws Exception {

        Book book = createBook();
        book.setId(0L);
        when(bookService.findBookById(0L)).thenReturn(Optional.empty());
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/")
                        .content(asJsonString(book))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(bookService, times(0)).updateBook(book);
    }

    @Test
    void updateBookByIdTestNotFound_InvalidTest() throws Exception {
        Book book = createBook();
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/3")
                        .content(asJsonString(book))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(bookService, times(0)).updateBook(book);
    }

    @Test
    void deleteBookByIdTest() throws Exception {
        Optional<Book> book = Optional.of(createBook());
        when(bookService.findBookById(anyLong())).thenReturn(book);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(book))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteBookById(anyLong());
    }

    @Test
    void deleteBookById_InvalidTest() throws Exception {
        Optional<Book> book = Optional.of(createBook());
        book.get().setId(0L);
        when(bookService.findBookById(0L)).thenReturn(book);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/0")
                        .content(asJsonString(book))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(bookService, times(0)).deleteBookById(0L);
    }

    @Test
    void deleteBookByIdNotFound_InvalidTest() throws Exception {
        Optional<Book> book = Optional.of(createBook());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/3")
                        .content(asJsonString(book))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(bookService, times(0)).deleteBookById(3L);
    }

    private List<Book> createBookList() {
        List<Book> list = new ArrayList<>();
        Book book1 = createBook();
        Book book2 = createBook();
        list.add(book1);
        list.add(book2);
        return list;
    }

    private Book createBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title1");
        book.setAuthor("Author1");
        book.setGenre("Genre1");
        book.setReleaseYear(2022L);
        book.setIsbn("000-00-00-0001");
        book.setQuantity(2L);
        book.setPrice(4L);
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