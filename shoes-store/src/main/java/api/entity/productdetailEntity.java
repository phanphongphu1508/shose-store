package api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Collection;

@Data
@Entity
@Table(name = "productdetail")
public class productdetailEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private productsEntity productsEntity;

    @NotNull
    @Min(35)
    private int size;

    @NotNull
    @Min(0)
    private long inventory;

    @OneToMany(mappedBy = "productdetail")
    private Collection<repositoryEntity> repositoryEntities;


    @OneToMany(mappedBy = "productdetail")
    private Collection<shopcartEntity> shopcartproduct;


    @OneToMany(mappedBy = "productdetailEntity", cascade = CascadeType.ALL)
    private Collection<orderdetailEntity> orderdetailEntities;
}

