package api.DTO;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class orderdetailDTO {

    private Long id;

    @NotNull
    @Min(1)
    private int quantity;

    @NotNull
    @Min(35)
    @Max(45)
    private int size;

    @NotBlank
    private long productid;

    @NotNull
    private float price;
}
