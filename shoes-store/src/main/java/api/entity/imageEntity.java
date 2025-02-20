package api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;


@Data
@Entity
@Table(name = "image")
public class imageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @URL
    @NotBlank
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private productsEntity productsEntity;
}
