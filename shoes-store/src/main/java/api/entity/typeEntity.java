package api.entity;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
