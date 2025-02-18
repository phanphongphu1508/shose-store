package api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@Table(name = "discount")
public class discountEntity {
    @Id
    private String id;

    @NotNull
    private float percent;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date deadline;

    @OneToMany(mappedBy = "discountEntitys", fetch = FetchType.LAZY)
    private Collection<productsEntity> productsEntities;
}
