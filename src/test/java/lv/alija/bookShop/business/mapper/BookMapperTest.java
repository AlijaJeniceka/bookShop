package lv.alija.bookShop.business.mapper;

import lv.alija.bookShop.business.repository.model.BookDAO;
import lv.alija.bookShop.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookMapperTest {

    private BookMapperImpl bookMapper;

    @BeforeEach
    public void setUp(){
        bookMapper = new BookMapperImpl();
    }

    @Test
    void bookToBookDAOTest() {
        Book book = new Book();
        book.setId(3L);
        book.setTitle("Title");
        book.setAuthor("Author1");
        book.setGenre("Genre1");
        book.setReleaseYear(2022L);
        book.setIsbn("000-00-00-0001");
        book.setQuantity(2L);
        book.setPrice(4L);
        BookDAO bookDAO = bookMapper.bookToBookDAO(book);
        assertEquals(bookDAO.getId(), book.getId());
        assertEquals(bookDAO.getTitle(), book.getTitle());
        assertEquals(bookDAO.getAuthor(), book.getAuthor());
        assertEquals(bookDAO.getGenre(), book.getGenre());
        assertEquals(bookDAO.getReleaseYear(), book.getReleaseYear());
        assertEquals(bookDAO.getIsbn(), book.getIsbn());
        assertEquals(bookDAO.getQuantity(), book.getQuantity());
        assertEquals(bookDAO.getPrice(), book.getPrice());
    }

    @Test
    void bookToBookDAOInvalidTest(){
        Book book = null;
        BookDAO bookDAO = bookMapper.bookToBookDAO(book);
        assertNull(null);
    }

    @Test
    void bookDAOToBookTest() {
        BookDAO bookDAO = new BookDAO();
        bookDAO.setId(4L);
        bookDAO.setTitle("Title");
        bookDAO.setAuthor("Author1");
        bookDAO.setGenre("Genre1");
        bookDAO.setReleaseYear(2022L);
        bookDAO.setIsbn("000-00-00-0001");
        bookDAO.setQuantity(2L);
        bookDAO.setPrice(4L);
        Book book = bookMapper.bookDAOToBook(bookDAO);
        assertEquals(book.getId(), bookDAO.getId());
        assertEquals(book.getTitle(), bookDAO.getTitle());
        assertEquals(book.getAuthor(), bookDAO.getAuthor());
        assertEquals(book.getGenre(), bookDAO.getGenre());
        assertEquals(book.getReleaseYear(), bookDAO.getReleaseYear());
        assertEquals(book.getIsbn(), bookDAO.getIsbn());
        assertEquals(book.getQuantity(), bookDAO.getQuantity());
        assertEquals(book.getPrice(), bookDAO.getPrice());
    }
    @Test
    void bookDAOToBookInvalidTest(){
        BookDAO bookDAO = null;
        Book book = bookMapper.bookDAOToBook(bookDAO);
        assertNull(null);
    }
}