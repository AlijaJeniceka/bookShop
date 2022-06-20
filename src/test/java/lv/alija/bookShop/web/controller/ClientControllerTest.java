package lv.alija.bookShop.web.controller;

import lv.alija.bookShop.business.service.impl.ClientServiceImpl;
import lv.alija.bookShop.model.Client;
import lv.alija.bookShop.model.enums.ClientTypes;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static lv.alija.bookShop.web.controller.BookControllerTest.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {
    public static String URL = "/client";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientController clientController;

    @MockBean
    private ClientServiceImpl clientService;

    @MockBean
    private KieSession kieSession;

    @Test
    void findAllClientsTest() throws Exception {
        List<Client> clientList  = createClientList();
        when(clientService.findAllClients()).thenReturn(clientList);
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/list"))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(25L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("PERSON"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].discount").value(5L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].documentForDiscount").value(true))
                .andExpect(status().isOk());
        verify(clientService, times(1)).findAllClients();
    }

    @Test
    void saveClientTest() throws Exception {
        Client client = createClient();
        when(clientService.saveClient(client)).thenReturn(client);
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(client))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(clientService, times(1)).saveClient(client);
    }
    @Test
    void saveClient_InvalidTest() throws Exception {
        Client client = createClient();
        client.setAge(-1L);
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(client))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(clientService, times(0)).saveClient(client);
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

}