package api.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Data
public class infoOrderDTO {

    @NotBlank
    private String fullname;

    @NotBlank
    private String address;

    @NotBlank
    private String email;

    @NotBlank
    @Size(max = 10, min = 9)
    private String phone;

    @NotNull
    private long paymentid;

    @NotNull
    private List<shopcartDTO> shopcart;

}
