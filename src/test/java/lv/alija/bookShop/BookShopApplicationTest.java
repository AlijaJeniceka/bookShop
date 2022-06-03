package lv.alija.bookShop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookShopApplicationTest {

    @Test
    void applicationStarts() {
        BookShopApplication.main(new String[] {});

    }
}