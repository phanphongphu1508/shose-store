package api.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
@Data
@Entity
@Table(name ="customers")
public class customersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 2,max = 50)
    private String firstname;

    @Size(min = 2,max = 50)
    private String lastname;

    @Size(min = 3,max = 200)
    private String address;

    @Size(min = 9,max = 10)
    private String phone;

    private String avatar;

    @OneToOne
    @JoinColumn(name ="users_id")
    private usersEntity usersEntitys;

    @OneToMany(mappedBy = "customersEntity")
    private Collection<ordersEntity> ordersEntities;

    @OneToMany(mappedBy = "customers")
    private Collection<wishlistEntity> wishlistEntities;

    @OneToMany(mappedBy = "customers")
    private Collection<shopcartEntity> customersproducts;
}
