package lv.alija.bookShop.business.service;

import lv.alija.bookShop.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<Client> findAllClients();

    Optional<Client> findClientsById(Long id);

    Client saveClient(Client client) throws Exception;

    void deleteClientById(Long id);

    Client updateClient(Client client) throws Exception;
}
