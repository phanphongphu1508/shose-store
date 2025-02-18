package api.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "payment")
public class paymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

//    @OneToOne(mappedBy = "paymentEntity")
//    private Collection<ordersEntity> ordersEntities;

    public paymentEntity() {
    }

    public paymentEntity(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
}
