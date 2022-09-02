package lv.alija.bookShop.web.controller;

import lv.alija.bookShop.business.repository.BookRepository;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
class BookControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void contextLoads() throws JSONException {

        String response = this.restTemplate.getForObject("/book/list", String.class );
        JSONAssert.assertEquals("[{id:1}, {id:2}]", response, false);
    }
}