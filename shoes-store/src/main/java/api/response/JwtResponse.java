package api.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private long id;

    private String username;

    private String email;

    private List<String> roles;

    private String type = "Bearer";

    private String token;
}
