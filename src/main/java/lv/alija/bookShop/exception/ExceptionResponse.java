package lv.alija.bookShop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ExceptionResponse{

    private String message;
    private Date timestamp;
    private String detail;


    public ExceptionResponse(ExceptionResponse exceptionResponse, HttpStatus badRequest) {
    }
}
