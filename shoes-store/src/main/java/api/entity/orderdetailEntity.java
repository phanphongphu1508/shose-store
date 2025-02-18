package api.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
