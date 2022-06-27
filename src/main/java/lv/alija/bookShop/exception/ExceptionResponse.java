package lv.alija.bookShop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

    private String message;
    private Date timestamp;
    private String detail;

}
