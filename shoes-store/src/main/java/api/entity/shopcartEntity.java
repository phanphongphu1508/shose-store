package api.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "shopcart")
public class shopcartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("customersid")
    @JoinColumn(name = "customersid")
    private customersEntity customers;

    @ManyToOne
    @JoinColumn(name = "productdetailid")
    private productdetailEntity productdetail;

    @NotNull
    @Min(1)
    private int quantity;
}
