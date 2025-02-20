package api.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class wishlistDTO {

    private long id;

    @NotNull
    private long productid;

    @NotNull
    private long customerid;
}
