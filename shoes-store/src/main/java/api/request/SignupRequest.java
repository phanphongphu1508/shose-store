package api.request;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {

    @NotBlank
    @Size(min = 4, max = 100)
    private String password;


    @NotBlank
    @Size(min = 8, max = 50)
    @Email
    private String email;


    @NotBlank
    @Size(min = 2, max = 50)
    private String firstname;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastname;

    @NotBlank
    @Size(min = 3, max = 200)
    private String address;

    @Size(min = 9, max = 10, message = "size not comfirm")
    private String phone;

    private Set<String> role;
}
