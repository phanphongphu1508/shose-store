package api.repository;

import api.entity.typeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface typeRepository extends JpaRepository<typeEntity, String> {

    @Override
    boolean existsById(String id);

    @Override
    Optional<typeEntity> findById(String id);

}
