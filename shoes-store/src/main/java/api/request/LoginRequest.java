package api.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
public class LoginRequest {
    @NotBlank(message = "ERR_USERNAME_E001")
    private String username;

    @NotBlank
    @Size(min = 3, max = 50, message = "")
    private String password;
}
