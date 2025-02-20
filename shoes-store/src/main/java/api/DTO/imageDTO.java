package api.DTO;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class imageDTO {

    @Min(value = 0)
    private long id;

    @URL
    private String url;

    private long productid;
}
