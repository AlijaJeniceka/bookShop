package lv.alija.library.business.mapper;

import lv.alija.library.business.repository.model.BookDAO;
import lv.alija.library.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDAO bookToBookDAO(Book book);

    Book bookDAOToBook(BookDAO bookDAO);


}
