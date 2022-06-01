package lv.alija.bookShop.business.mapper;

import lv.alija.bookShop.business.repository.model.BookDAO;
import lv.alija.bookShop.business.repository.model.ClientDAO;
import lv.alija.bookShop.model.Book;
import lv.alija.bookShop.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDAO clientToClientDAO(Client client);

    Client clientDAOToClient(ClientDAO clientDAO);
}
