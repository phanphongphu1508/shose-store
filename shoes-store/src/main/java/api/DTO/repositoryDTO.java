package api.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
@Data
public class repositoryDTO {
    private String id;

    @NotBlank
    private String typeid;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date datecreated;

    @NotNull
    private productdetailDTO productdetail;

    @NotNull
    @Min(1)
    private  int quantity;

    @NotNull
    private  float price;

    private  String createdBy;
}
