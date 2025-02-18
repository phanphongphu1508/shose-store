package api.repository;

import api.entity.categoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface categoryRepository extends JpaRepository<categoryEntity, Long> {

    @Override
    Optional<categoryEntity> findById(Long id);

    @Override
    boolean existsById(Long id);

    // Phương thức tìm kiếm theo tên
    categoryEntity findByName(String name);

}
