package api.repository;

import api.entity.discountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface discountRepository extends JpaRepository<discountEntity, String> {

    @Override
    boolean existsById(String id);

    @Override
    Optional<discountEntity> findById(String id);

}
