package lv.alija.bookShop.business.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lv.alija.bookShop.business.mapper.ClientMapper;
import lv.alija.bookShop.business.repository.ClientRepository;
import lv.alija.bookShop.business.repository.model.ClientDAO;
import lv.alija.bookShop.business.service.ClientService;
import lv.alija.bookShop.model.Client;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {


   private final ClientMapper clientMapper;
   private final  ClientRepository clientRepository;

    @Override
    public List<Client> findAllClients() {
        return null;
    }

    @Override
    public Optional<Client> findClientsById(Long id) {
        return Optional.empty();
    }

    @Override
    public Client saveClient(Client client) throws Exception {
        ClientDAO clientDAO = clientMapper.clientToClientDAO(client);
        ClientDAO clientSaved = clientRepository.save(clientDAO);
        log.info("Client is created: {}", () -> clientSaved);
        return clientMapper.clientDAOToClient(clientSaved);
    }

    @Override
    public void deleteClientById(Long id) {
    }

    @Override
    public Client updateClient(Client client) throws Exception {
        return null;
    }
}
