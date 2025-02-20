package api.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Collection;

@Data
@Entity
@Table(name = "type")
public class typeEntity {

    @Id
    private String id;

    @NotBlank
    private String nametype;


    @OneToMany(mappedBy = "type")
    private Collection<repositoryEntity> repositoryEntities;
}
