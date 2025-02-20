package api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "orderdetail")
public class orderdetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productdetailid")
    private productdetailEntity productdetailEntity;

    @NotNull
    @Min(1)
    private int quantity;

    @NotNull
    private float price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private ordersEntity orders;
}
