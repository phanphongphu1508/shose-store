package api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Collection;

@Data
@Entity
@Table(name = "customers")
public class customersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 2, max = 50)
    private String firstname;

    @Size(min = 2, max = 50)
    private String lastname;

    @Size(min = 3, max = 200)
    private String address;

    @Size(min = 9, max = 10)
    private String phone;

    private String avatar;

    @OneToOne
    @JoinColumn(name = "users_id")
    private usersEntity usersEntitys;

    @OneToMany(mappedBy = "customersEntity")
    private Collection<ordersEntity> ordersEntities;

    @OneToMany(mappedBy = "customers")
    private Collection<wishlistEntity> wishlistEntities;

    @OneToMany(mappedBy = "customers")
    private Collection<shopcartEntity> customersproducts;


    public customersEntity(@NotBlank @Size(min = 2, max = 50) String firstname, @NotBlank @Size(min = 2, max = 50) String lastname, @NotBlank @Size(min = 3, max = 200) String address, @Size(min = 9, max = 10, message = "size not comfirm") String phone, String linkphotodefault, usersEntity user) {
    }

    public customersEntity() {
        
    }
}
