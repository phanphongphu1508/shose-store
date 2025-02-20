package api.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
public class productsDTO {

    private long id;

    @NotBlank
    @Size(min = 5,max =100, message = "ERR_PDNM_E001")
    private String name;

    @NotBlank
    @Size(min = 5,max =500, message = "ERR_DES_E001")
    private String description;

    @NotNull(message = "ERR_PRICE_E001")
    @Min(value = 1000, message = "ERR_PRICE_E001")
    private float price;

    @NotBlank(message = "ERR_COLOR_E001")
    private String color;

    private List<productdetailDTO> listsize;

    private String discount;

    private int rating;

    @NotBlank(message = "ERR_STATUS_E001")
    private String status;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date deadline;

    @NotBlank(message = "ERR_UNI_E001")
    private String unitype;

    @NotBlank(message = "ERR_CRTBY_E001")
    private String createdby;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createddate;

    private String modifiedby;

    private Date modifieddate;

    @NotNull
    private long categoryid;

    private String categoryname;

    @Size(min = 1,max = 5, message = "ERR_IMG_E001,ERR_IMG_E002")
    private List<imageDTO> listimage = new ArrayList<>();
}

