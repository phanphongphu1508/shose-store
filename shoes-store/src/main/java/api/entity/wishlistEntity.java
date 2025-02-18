package api.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "wishlist")
public class wishlistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private productsEntity products;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private customersEntity customers;
}