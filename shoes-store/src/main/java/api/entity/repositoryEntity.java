package api.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "repository")
public class repositoryEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private typeEntity type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productdetailid")
    private productdetailEntity productdetail;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date datecreated;

    @NotNull
    @Min(1)
    private int quantity;

    @NotNull
    private float price;

    private String createdBy;
}
