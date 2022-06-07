package lv.alija.bookShop.business.mapper;

import lv.alija.bookShop.business.repository.model.ClientDAO;
import lv.alija.bookShop.model.Client;
import lv.alija.bookShop.model.enums.ClientTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class ClientMapperTest {

    private ClientMapperImpl clientMapper;

    @BeforeEach
    public void setUp(){
        clientMapper = new ClientMapperImpl();
    }

    @Test
    void clientToClientDAOTest() {
        Client client = new Client();
        client.setId(1L);
        client.setAge(25L);
        client.setType(ClientTypes.PERSON);
        client.setDiscount(5L);
        client.setDocumentForDiscount(true);
        ClientDAO clientDAO = clientMapper.clientToClientDAO(client);
        assertEquals(clientDAO.getId(), client.getId());
        assertEquals(clientDAO.getAge(), client.getAge());
        assertEquals(clientDAO.getType(), client.getType());
        assertEquals(clientDAO.getDiscount(), client.getDiscount());
    }
    @Test
    void clientToClientDAOInvalidTest() {
        Client client = null;
        ClientDAO clientDAO = clientMapper.clientToClientDAO(client);
        assertNull(null);
    }

    @Test
    void clientDAOToClientTest() {
        ClientDAO clientDAO = new ClientDAO();
        clientDAO.setId(1L);
        clientDAO.setAge(25L);
        clientDAO.setType(ClientTypes.PERSON);
        clientDAO.setDiscount(5L);
        clientDAO.setDocumentForDiscount(true);
        Client client = clientMapper.clientDAOToClient(clientDAO);
        assertEquals(client.getId(), clientDAO.getId());
        assertEquals(client.getAge(), clientDAO.getAge());
        assertEquals(client.getType(), clientDAO.getType());
        assertEquals(client.getDiscount(), clientDAO.getDiscount());
    }
    @Test
    void clientDAOToClientInvalidTest() {
        ClientDAO clientDAO = null;
        Client client = clientMapper.clientDAOToClient(clientDAO);
        assertNull(null);
    }
}