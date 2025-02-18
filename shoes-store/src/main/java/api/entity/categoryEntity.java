package api.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;

@Data
@Entity
@Table(name = "catogory")
public class categoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50)
    private String name;

    @Size(min = 5, max = 500)
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Collection<productsEntity> productsEntities;
}
