package lv.alija.bookShop.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(description = "Model of book data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {

    @ApiModelProperty(notes = "The unique id of the book should be more then 0")
    @Min(value = 1)
    private Long id;

    @ApiModelProperty(notes = "Title of the book")
    @NotNull
    @NotBlank(message = "Please fill the field - title")
    private String title;

    @ApiModelProperty(notes = "Author of the book")
    @NotNull
    @NotBlank(message = "Please fill the field - author")
    private String author;

    @ApiModelProperty(notes = "Genre of the book")
    @NotNull
    @NotBlank(message = "Please fill the field - genre")
    private String genre;

    @ApiModelProperty(notes = "Book release year")
    @NotNull
    private Long releaseYear;

    @ApiModelProperty(notes = "Book ISBN number")
    @NotNull
    @NotBlank(message = "Please fill the field - isbn")
    private String isbn;

    @ApiModelProperty(notes = "Book quantity")
    @Min(value = 0)
    private Long quantity;

    @ApiModelProperty(notes = "Book price")
    @Min(value = 0)
    @NotNull
    private Long price;
}
