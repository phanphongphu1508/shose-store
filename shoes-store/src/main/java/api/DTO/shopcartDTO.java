package api.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class shopcartDTO {

    private long id;

    private long customersid;

    @NotNull
    private productdetailDTO productdetail;

    @NotNull
    @Min(1)
    private int quantity;
}
