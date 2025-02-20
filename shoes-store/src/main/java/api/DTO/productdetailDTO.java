package api.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class productdetailDTO {

    private long id;

    private long productid;

    @NotNull
    @Min(35)
    private int size;

    private long inventory;
}
