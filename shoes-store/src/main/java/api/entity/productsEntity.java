package api.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class productsEntity {
    public enum Status {
        INACTIVE, ACTIVE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String color;

    @NotBlank
    @Size(min = 5, max = 100)
    private String name;

    @NotBlank
    @Size(min = 5, max = 500)
    private String description;

    @NotNull()
    private float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private discountEntity discountEntitys;

    private int rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    private Date deadline;

    @NotNull
    private String unitype;

    @NotBlank
    private String createdby;

    private Date createddate;

    private String modifiedby;

    private Date modifieddate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private categoryEntity category;

    @OneToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private Collection<wishlistEntity> wishlistEntities;

    @OneToMany(mappedBy = "productsEntity", cascade = CascadeType.ALL)
    private List<imageEntity> imageEntities = new ArrayList<>();

    @OneToMany(mappedBy = "productsEntity", cascade = CascadeType.ALL)
    private List<productdetailEntity> productdetailEntities = new ArrayList<>();
    ;
}
