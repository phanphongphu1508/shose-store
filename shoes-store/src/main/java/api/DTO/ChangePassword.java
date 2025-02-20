package api.DTO;

import lombok.Data;

@Data
public class ChangePassword {
    private String oldpassword;
    private String newpassword;
    private String confirmpassword;
}
