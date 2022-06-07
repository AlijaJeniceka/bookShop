package lv.alija.bookShop.business.service;

import lv.alija.bookShop.model.Client;

import java.util.List;

public interface ClientService {

    List<Client> findAllClients();


    Client saveClient(Client client) throws Exception;
}

