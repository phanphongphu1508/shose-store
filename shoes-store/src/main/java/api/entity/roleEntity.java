package api.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role")
public class roleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private role_name name;
}
