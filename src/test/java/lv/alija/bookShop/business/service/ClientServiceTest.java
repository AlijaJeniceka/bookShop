package lv.alija.bookShop.business.service;

import lv.alija.bookShop.business.mapper.ClientMapper;
import lv.alija.bookShop.business.repository.ClientRepository;
import lv.alija.bookShop.business.repository.model.ClientDAO;
import lv.alija.bookShop.business.service.impl.ClientServiceImpl;
import lv.alija.bookShop.model.Client;
import lv.alija.bookShop.model.enums.ClientTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ClientServiceTest {

    @InjectMocks
    ClientServiceImpl clientService;
    @Spy
    ClientRepository clientRepository;
    @Spy
    ClientMapper clientMapper;

    private Client client;
    private ClientDAO clientDAO;
    private List<Client> clientList;
    private List<ClientDAO> clientDAOList;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @BeforeEach
    void init(){
        clientDAO = createClientDAO();
        client = createClient();
        clientDAOList = createClientListDAO();
        clientList = createClientList();
    }
    @Test
    void findAllClients() {
        when(clientRepository.findAll()).thenReturn(clientDAOList);
        when(clientMapper.clientDAOToClient(clientDAO)).thenReturn(client);
        List<Client> clients = clientService.findAllClients();
        assertEquals(2, clients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void findAllClients_InvalidTest(){
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(clientService.findAllClients().isEmpty());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void saveClient() throws Exception {
        when(clientRepository.save(clientDAO)).thenReturn(clientDAO);
        when(clientMapper.clientDAOToClient(clientDAO)).thenReturn(client);
        when(clientMapper.clientToClientDAO(client)).thenReturn(clientDAO);
        Client savedClient = clientService.saveClient(client);
        assertEquals(savedClient, client);
        verify(clientRepository, times(1)).save(clientDAO);
    }

    @Test
    void saveClient_InvalidTest(){
        when(clientRepository.save(clientDAO)).thenThrow(new IllegalArgumentException());
        when(clientMapper.clientToClientDAO(client)).thenReturn(clientDAO);
        assertThrows(IllegalArgumentException.class, () -> clientService.saveClient(client));
        verify(clientRepository, times(1)).save(clientDAO);
    }

    private List<Client> createClientList(){
        List<Client> clientList = new ArrayList<>();
        Client client1 = createClient();
        Client client2 = createClient();
        clientList.add(client1);
        clientList.add(client2);
        return clientList;
    }
    private Client createClient(){
        Client client = new Client();
        client.setId(1L);
        client.setAge(25L);
        client.setType(ClientTypes.PERSON);
        client.setDiscount(5L);
        client.setDocumentForDiscount(true);
        return client;
    }
    private List<ClientDAO> createClientListDAO(){
        List<ClientDAO> clientDAOList = new ArrayList<>();
        ClientDAO clientDAO1 = createClientDAO();
        ClientDAO clientDAO2 = createClientDAO();
        clientDAOList.add(clientDAO1);
        clientDAOList.add(clientDAO2);
        return clientDAOList;
    }
    private ClientDAO createClientDAO(){
        ClientDAO clientDAO = new ClientDAO();
        clientDAO.setId(1L);
        clientDAO.setAge(25L);
        clientDAO.setType(ClientTypes.PERSON);
        clientDAO.setDiscount(5L);
        clientDAO.setDocumentForDiscount(true);
        return clientDAO;
    }
}