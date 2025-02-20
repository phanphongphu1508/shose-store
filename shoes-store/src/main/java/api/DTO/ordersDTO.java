package api.DTO;

import api.entity.paymentEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Data
public class ordersDTO {

    private String id;

    @NotNull
    private float total;

    @NotBlank
    private String status;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

    @Email
    private String email;

    private String fullname;

    private String createdBy;

    private Date createdDate;

    private String modifiedBy;

    private Date modifiedDate;

    private paymentEntity paymentEntity;

    private List<orderdetailDTO> listOrderdetail;
}
