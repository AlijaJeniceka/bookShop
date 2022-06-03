package lv.alija.bookShop.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.alija.bookShop.model.enums.ClientTypes;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model of client data")
public class Client {

    @ApiModelProperty(notes = "The unique id for the client")
    @Min(value = 1)
    private Long id;

    @ApiModelProperty(notes = "Client's type")
    @NotNull
    private ClientTypes type;

    @ApiModelProperty(notes = "Client's age")
    @NotNull
    private Long age;

    @ApiModelProperty(notes = "Client's discount")
    private Long discount;

    @ApiModelProperty(notes = "Client have or not document to receive discount")
    private boolean documentForDiscount;

}
