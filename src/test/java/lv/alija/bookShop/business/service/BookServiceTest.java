package lv.alija.bookShop.business.service;

import lv.alija.bookShop.business.mapper.BookMapper;
import lv.alija.bookShop.business.repository.BookRepository;
import lv.alija.bookShop.business.repository.model.BookDAO;
import lv.alija.bookShop.business.service.impl.BookServiceImpl;
import lv.alija.bookShop.exception.BookControllerException;
import lv.alija.bookShop.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class BookServiceTest {

    @InjectMocks
    BookServiceImpl bookService;

    @Spy
    BookRepository bookRepository;

    @Spy
    BookMapper bookMapper;

    private Book book;
    private BookDAO bookDAO;
    private List<Book> bookList;
    private List<BookDAO> bookDAOList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void init() {
        bookDAO = createBookDAO();
        book = createBook();
        bookDAOList = createBookDAOList();
        bookList = createBookList();
    }

    @Test
    void findAllBooks() {
        when(bookRepository.findAll()).thenReturn(bookDAOList);
        when(bookMapper.bookDAOToBook(bookDAO)).thenReturn(book);
        List<Book> books = bookService.findAllBooks();
        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void findAllBooks_EmptyList_InvalidTest(){
       when(bookRepository.findAll()).thenReturn(Collections.emptyList())
               .thenThrow(new BookControllerException(HttpStatus.NOT_FOUND, "Book list is empty"));
       assertThrows(BookControllerException.class, () -> bookService.findAllBooks());
       verify(bookRepository, times(1)).findAll();
    }

    @Test
    void findBookListByAuthorTest() {
        when(bookRepository.findByAuthor("Author1")).thenReturn(bookDAOList);
        when(bookMapper.bookDAOToBook(bookDAO)).thenReturn(book);
        List<Book> books = bookService.findByAuthor("Author1");
        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findByAuthor("Author1");
    }

    @Test
    void findBookListByAuthor_EmptyList_InvalidTest() {
        when(bookRepository.findByAuthor("")).thenReturn(Collections.emptyList())
                .thenThrow(new BookControllerException(HttpStatus.NOT_FOUND, "Book list is empty"));
        assertThrows(BookControllerException.class, () -> bookService.findByAuthor(""));
        verify(bookRepository, times(1)).findByAuthor("");
    }

    @Test
    void findBookByIdTest() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookDAO));
        when(bookMapper.bookDAOToBook(bookDAO)).thenReturn(book);
        Optional<Book> bookById = bookService.findBookById(book.getId());
        assertEquals(book.getId(), bookById.get().getId());
        assertEquals(book.getAuthor(), bookById.get().getAuthor());
        assertEquals(book.getTitle(), bookById.get().getTitle());
        assertEquals(book.getGenre(), bookById.get().getGenre());
        assertEquals(book.getReleaseYear(), bookById.get().getReleaseYear());
        assertEquals(book.getIsbn(), bookById.get().getIsbn());
        assertEquals(book.getQuantity(), bookById.get().getQuantity());
        assertEquals(book.getPrice(), bookById.get().getPrice());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void findBookById_idNegativeOrNull_InvalidTest() {
        assertThrows(BookControllerException.class, () -> bookService.findBookById(-1L));
        verify(bookRepository, times(0)).findById(-1L);
    }

    @Test
    void findBookById_idPositiveButNotFound_InvalidTest() {
        assertThrows(BookControllerException.class, () -> bookService.findBookById(5L));
        verify(bookRepository, times(1)).findById(5L);
    }

    @Test
    void saveBookTest() throws Exception {
        when(bookRepository.save(bookDAO)).thenReturn(bookDAO);
        when(bookMapper.bookDAOToBook(bookDAO)).thenReturn(book);
        when(bookMapper.bookToBookDAO(book)).thenReturn(bookDAO);
        Book bookSaved = bookService.saveBook(book);
        assertEquals(book, bookSaved);
        verify(bookRepository, times(1)).save(bookDAO);
    }

    @Test
    void saveBookTest_InvalidTest() {
        when(bookRepository.save(bookDAO)).thenThrow(new IllegalArgumentException());
        when(bookMapper.bookToBookDAO(book)).thenReturn(bookDAO);
        assertThrows(IllegalArgumentException.class, () -> bookService.saveBook(book));
        verify(bookRepository, times(1)).save(bookDAO);
    }

    @Test
    void saveBookTest_InvalidDuplicateTest() throws Exception {
        Book bookSaved = createBook();
        bookSaved.setId(1L);
        bookSaved.setAuthor("Author1");
        bookSaved.setTitle("Title1");
        bookSaved.setGenre("Genre1");
        bookSaved.setReleaseYear(2022L);
        bookSaved.setIsbn("000-00-00-0001");
        bookSaved.setQuantity(2L);
        bookSaved.setPrice(4L);
        when(bookRepository.findAll()).thenReturn(bookDAOList);
        assertThrows(BookControllerException.class, () -> bookService.saveBook(bookSaved));
        verify(bookRepository, times(0)).save(bookDAO);
    }

    @Test
    void deleteBookByIdTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookDAO));
        when(bookMapper.bookDAOToBook(bookDAO)).thenReturn(book);
        Optional<Book> bookById = bookService.findBookById(book.getId());
        bookService.deleteBookById(bookById.get().getId());
        verify(bookRepository, times(1)).deleteById(bookById.get().getId());
    }

    @Test
    void deleteBookById_idPositiveButNotFound_InvalidTest() {
        assertThrows(BookControllerException.class,
                () -> bookService.deleteBookById(56L));
        verify(bookRepository, times(1)).findById(56L);
        verify(bookRepository, times(0)).deleteById(56L);
    }
    @Test
    void deleteBookById_idNegativeOrNull_InvalidTest() {
        assertThrows(BookControllerException.class,
                () -> bookService.deleteBookById(-1L));
        verify(bookRepository, times(0)).findById(-1L);
        verify(bookRepository, times(0)).deleteById(-1L);
    }

    @Test
    void updateBookTest() throws Exception {
        when(bookRepository.save(bookDAO)).thenReturn(bookDAO);
        when(bookMapper.bookDAOToBook(bookDAO)).thenReturn(book);
        when(bookMapper.bookToBookDAO(book)).thenReturn(bookDAO);
        Book bookSaved = bookService.updateBook(book, 1L);
        assertEquals(book, bookSaved);
        verify(bookRepository, times(1)).save(bookDAO);
    }

    @Test
    void updateBookTest_WithIncorrectId_InvalidTest() {
        assertThrows(BookControllerException.class, () -> bookService.updateBook(book, 3L));
        verify(bookRepository, times(0)).save(bookDAO);
    }

    private List<BookDAO> createBookDAOList() {
        List<BookDAO> listDAO = new ArrayList<>();
        BookDAO bookDAO1 = createBookDAO();
        BookDAO bookDAO2 = createBookDAO();
        listDAO.add(bookDAO1);
        listDAO.add(bookDAO2);
        return listDAO;
    }

    private BookDAO createBookDAO() {
        BookDAO bookDAO = new BookDAO();
        bookDAO.setId(1L);
        bookDAO.setAuthor("Author1");
        bookDAO.setTitle("Title1");
        bookDAO.setGenre("Genre1");
        bookDAO.setReleaseYear(2022L);
        bookDAO.setIsbn("000-00-00-0001");
        bookDAO.setQuantity(2L);
        bookDAO.setPrice(4L);
        return bookDAO;
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
        book.setAuthor("Author1");
        book.setTitle("Title1");
        book.setGenre("Genre1");
        book.setReleaseYear(2022L);
        book.setIsbn("000-00-00-0001");
        book.setQuantity(2L);
        book.setPrice(4L);
        return book;
    }
}