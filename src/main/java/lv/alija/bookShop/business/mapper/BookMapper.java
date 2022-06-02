package lv.alija.bookShop.business.mapper;

import lv.alija.bookShop.business.repository.model.BookDAO;
import lv.alija.bookShop.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDAO bookToBookDAO(Book book);

    Book bookDAOToBook(BookDAO bookDAO);


}
