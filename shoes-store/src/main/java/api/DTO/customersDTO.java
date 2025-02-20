package api.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
public class customersDTO {

    private long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstname;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastname;

    @NotBlank
    @Size(min = 3, max = 200)
    private String address;

    @NotBlank
    @Size(max = 10, min = 9)
    private String phone;

    @NotBlank
    private String email;

    private String avatar;
}
