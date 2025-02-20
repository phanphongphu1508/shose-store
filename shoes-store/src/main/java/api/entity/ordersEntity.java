package api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class ordersEntity {
    public enum Status {
        DELIVERING, DELIVERED, UNCONFIRM, CANCEL;
    }

    @Id
    private String id;

    @NotNull
    private float total;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

    @Email
    private String email;

    @NotBlank
    private String fullname;

    private String createdBy;

    private Date createdDate;

    private String modifiedBy;

    private Date modifiedDate;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<orderdetailEntity> orderdetailEntities = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private customersEntity customersEntity;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private paymentEntity paymentEntity;
}
