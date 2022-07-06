package lv.alija.bookShop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class BookControllerException extends RuntimeException{
    private HttpStatus errorCode;
    private String message;

}
