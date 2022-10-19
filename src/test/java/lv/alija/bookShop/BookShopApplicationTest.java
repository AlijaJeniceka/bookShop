package lv.alija.bookShop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookShopApplicationTest {

    @Test
    void applicationStartsTest() {
        BookShopApplication.main(new String[] {});
    }
}